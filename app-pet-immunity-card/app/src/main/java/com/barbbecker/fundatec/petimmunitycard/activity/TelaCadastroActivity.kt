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
import com.barbbecker.fundatec.petimmunitycard.activity.TelaCadastroActivity.TelaCadastroActivity.GALLERY
import com.barbbecker.fundatec.petimmunitycard.activity.TelaCadastroActivity.TelaCadastroActivity.PICK_FROM_GALLERY
import com.barbbecker.fundatec.petimmunitycard.network.ServiceRetrofitPet
import com.barbbecker.fundatec.petimmunitycard.service.models.Pet
import com.barbbecker.fundatec.petimmunitycard.service.models.Session
import com.roger.catloadinglibrary.CatLoadingView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_tela_cadastro.*
import kotlinx.android.synthetic.main.content_tela_cadastro.*
import kotlinx.android.synthetic.main.content_tela_cadastro.textNamePet
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TelaCadastroActivity : AppCompatActivity() {
    object TelaCadastroActivity {
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
        menuInflater.inflate(R.menu.menu_cadastro_pet, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                savePets()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_cadastro)
        setSupportActionBar(toolbar)

        AWSMobileClient.getInstance().initialize(this) {
            Log.e("TAG", "AWSMobileClient is initialized")
        }.execute()

        imageViewPet.setOnClickListener {
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
                    imageViewPet.setImageBitmap(bitmap)
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

        val uploadObserver = transferUtility.upload("pet-immunity-card", s3key, file)

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

    fun savePets() {
        if (textNamePet.text.toString().isEmpty()) {
            textNamePet.error = getString(R.string.save_name_pet)
        }
        if (textBreed.text.toString().isEmpty()) {
            textBreed.error = getString(R.string.save_breed_pet)
        }
        if (textBirthDate.text.toString().isEmpty()) {
            textBirthDate.error = getString(R.string.save_birthday)
        }
        if (textNumberChip.text.toString().isEmpty()) {
            textNumberChip.error = getString(R.string.save_chip)
        }
        if (!textNamePet.text.toString().isEmpty() &&
            !textBreed.text.toString().isEmpty() &&
            !textBirthDate.text.toString().isEmpty() &&
            !textNumberChip.text.toString().isEmpty()
        ) {
            requestRegisterPet(Pet(
                textBirthDate.text.toString(), textBreed.text.toString(),
                s3key, null , textNamePet.text.toString(),
                textNumberChip.text.toString(), Session.cpf, listOf()
            ))
        }

    }

    fun requestRegisterPet(pet: Pet): Disposable {
        return ServiceRetrofitPet.requestRegisterPet(pet).subscribe({
            finish()
        }, { err ->
            showMessage(getString(R.string.error_register_pet))
        })
    }

    private fun showMessage(message:String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
