package com.barbbecker.fundatec.petimmunitycard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.barbbecker.fundatec.petimmunitycard.activity.TelaAddPerson
import com.barbbecker.fundatec.petimmunitycard.activity.TelaInicialActivity
import com.barbbecker.fundatec.petimmunitycard.network.ServiceRetrofitPet
import com.barbbecker.fundatec.petimmunitycard.service.models.Person
import com.barbbecker.fundatec.petimmunitycard.service.models.Session
import com.roger.catloadinglibrary.CatLoadingView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var loader = CatLoadingView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        buttonSave.setOnClickListener { view ->
            if (textEmail.text.toString().isEmpty()) {
                textEmail.error = getString(R.string.invalid_email)
            }
            if (textPassword.text.toString().isEmpty()) {
                textPassword.error = getString(R.string.invalid_password)
            }
            if (!textEmail.text.toString().isEmpty() &&
                !textPassword.text.toString().isEmpty()
            ) {
                requestLogin()
            }
        }

        buttonRegister.setOnClickListener { view ->
            startActivity(Intent(baseContext, TelaAddPerson::class.java))
        }
    }

    fun requestLogin(): Disposable {
        loader.show(supportFragmentManager, "cat_loading")

        val person = Person(textEmail.text.toString(), textPassword.text.toString())
        return ServiceRetrofitPet.requestLogin(person).subscribe({
            Session.cpf = it.cpf
            Session.personId = it.personId
            if (it.success) {
                loader.dismiss()

                startActivity(Intent(applicationContext, TelaInicialActivity::class.java))
            }
            else
                showMessage(getString(R.string.error_invalid_user))
        }, { err ->
            showMessage(getString(R.string.error_unexpected_error))
        })
    }

    private fun showMessage(message:String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}