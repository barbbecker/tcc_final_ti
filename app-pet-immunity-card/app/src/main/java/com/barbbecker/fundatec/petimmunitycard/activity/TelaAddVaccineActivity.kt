package com.barbbecker.fundatec.petimmunitycard.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.barbbecker.fundatec.petimmunitycard.R
import com.barbbecker.fundatec.petimmunitycard.activity.TelaAddVaccineActivity.TelaAddVaccineActivity.GALLERY
import com.barbbecker.fundatec.petimmunitycard.activity.TelaAddVaccineActivity.TelaAddVaccineActivity.PICK_FROM_GALLERY
import com.barbbecker.fundatec.petimmunitycard.network.ServiceRetrofitPet
import com.barbbecker.fundatec.petimmunitycard.service.models.Vaccine
import com.roger.catloadinglibrary.CatLoadingView
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_tela_add_vaccine.*
import kotlinx.android.synthetic.main.content_tela_add_vaccine.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TelaAddVaccineActivity : AppCompatActivity() {
    object TelaAddVaccineActivity {
        const val GALLERY = 0
        const val PICK_FROM_GALLERY = 1
    }

    private lateinit var s3key: String

    private var loader = CatLoadingView()

    private val credentials =
        BasicAWSCredentials("AKIAVTMCYYZAX2JSLDBD", "eclYIsKtU+rUleUubajzS7bJpO416ie3V22m0wyY")
    private val s3Client = AmazonS3Client(credentials)

    lateinit var file: File

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_add_vaccine, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.saveVaccine -> {
                saveVaccine()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_add_vaccine)
        setSupportActionBar(toolbar)

        AWSMobileClient.getInstance().initialize(this) {
            Log.e("TAG", "AWSMobileClient is initialized")
        }.execute()

        imageViewVaccine.setOnClickListener {
            showPictureDialog()
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        val pictureDialogItems = arrayOf(getString(R.string.gallery))
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                GALLERY -> choosePhotoFromGallary()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), PICK_FROM_GALLERY
                )
            } else {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, GALLERY)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY) {
            if (data?.data != null) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                    Toast.makeText(this, getString(R.string.process_image), Toast.LENGTH_SHORT).show()
                    imageViewVaccine.setImageBitmap(bitmap)
                    uploadFile(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, getString(R.string.not_process_image), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createFile(bitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)

        val dateFormat = SimpleDateFormat("yyyy_MM_dd", Locale.getDefault())
        val date = Date()
        val fileName = dateFormat.format(date) + "temp." + "jpg"
        file = File(baseContext.getCacheDir(), fileName)


        try {
            val fo = FileOutputStream(file)
            fo.write(bytes.toByteArray())
            fo.flush()
            fo.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun uploadFile(image: Bitmap) {
        loader.show(supportFragmentManager, "cat_loading")

        createFile(image)

        val transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(s3Client)
            .build()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        val date = Date()
        val fileName = dateFormat.format(date) + ".jpg"

        s3key = fileName

        val uploadObserver = transferUtility.upload("pet-immunity-card-vaccine", s3key, file)

        uploadObserver.setTransferListener(object : TransferListener {

            override fun onStateChanged(id: Int, state: TransferState) {
                Log.e("onStateChanged", "onStateChanged $state")

                //TODO: Aqui tem outros statatus caso tu queira trabalhar com eles
                if (TransferState.COMPLETED == state) {
                    loader.dismiss()

                    Toast.makeText(applicationContext, getString(R.string.upload), Toast.LENGTH_LONG).show()
                    file.delete()
                } else if (TransferState.FAILED == state) {
                    file.delete()
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                //TODO: Aqui seria interessante colocar um Loader ou uma barra de progresso para mostrar pro usuário
                // indicando que algo está ocorrendo

                Log.e("onProgressChanged", "onProgressChanged")
                val percentDonef = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                val percentDone = percentDonef.toInt()

                Log.e("teste", "$percentDone")
            }

            override fun onError(id: Int, ex: Exception) {
                ex.printStackTrace()
                Log.e("onError", "onError")
            }

        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PICK_FROM_GALLERY ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY)
                } else {
                    Toast.makeText(this, getString(R.string.permission_camera), Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun saveVaccine() {
        if (textLayoutNameVaccine.text.toString().isEmpty()) {
            textLayoutNameVaccine.error = getString(R.string.save_name_vaccine)
        }
        if (textLayoutNameVet.text.toString().isEmpty()) {
            textLayoutNameVet.error = getString(R.string.save_name_vet)
        }
        if (textLayoutDateApplication.text.toString().isEmpty()) {
            textLayoutDateApplication.error = getString(R.string.save_date_app)
        }
        if (textLayoutDateReapplication.text.toString().isEmpty()) {
            textLayoutDateReapplication.error = getString(R.string.save_date_reapp)
        }
        if (!textLayoutNameVaccine.text.toString().isEmpty() &&
            !textLayoutNameVet.text.toString().isEmpty() &&
            !textLayoutDateApplication.text.toString().isEmpty() &&
            !textLayoutDateReapplication.text.toString().isEmpty()
        ) {
            requestSendVaccine(Vaccine(null, textLayoutNameVaccine.text.toString(),
                textLayoutNameVet.text.toString(),
                textLayoutDateApplication.text.toString(),
                textLayoutDateReapplication.text.toString(), s3key), intent.getIntExtra("pet_id", 0))
        }
    }

    fun requestSendVaccine(vaccine: Vaccine, id:Int): Disposable {
        return ServiceRetrofitPet.requestRegisterVaccine(vaccine, id).subscribe({
            finish()
        }, { err ->
            showMessage(getString(R.string.error_register_vaccine))
        })
    }

    private fun showMessage(message:String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
