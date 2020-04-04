package com.barbbecker.fundatec.petimmunitycard.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.barbbecker.fundatec.petimmunitycard.R
import com.barbbecker.fundatec.petimmunitycard.network.ServiceRetrofitPet
import com.barbbecker.fundatec.petimmunitycard.service.models.Person
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.activity_tela_add_person.*
import kotlinx.android.synthetic.main.content_tela_add_person.*

class TelaAddPerson : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_add_person)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_add_person, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.savePerson -> {
                savePerson()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun savePerson() {
        if (textInputName.text.toString().isEmpty()) {
            textInputName.error = getString(R.string.save_name_person)
        }
        if (textInputAddress.text.toString().isEmpty()) {
            textInputAddress.error = getString(R.string.save_address_person)
        }
        if (textInputNumber.text.toString().isEmpty()) {
            textInputNumber.error = getString(R.string.save_number_address_person)
        }
        if (textInputPhone.text.toString().isEmpty()) {
            textInputPhone.error = getString(R.string.save_phone_person)
        }
        if (textInputCpf.text.toString().isEmpty()) {
            textInputCpf.error = getString(R.string.save_cpf_person)
        }
        if (textInputEmail.text.toString().isEmpty()) {
            textInputEmail.error = getString(R.string.save_email_person)
        }
        if (textPassword.text.toString().isEmpty()) {
            textPassword.error = getString(R.string.save_password_person)
        }
        if (!textInputName.text.toString().isEmpty() &&
            !textInputAddress.text.toString().isEmpty() &&
            !textInputNumber.text.toString().isEmpty() &&
            !textInputPhone.text.toString().isEmpty() &&
            !textInputCpf.text.toString().isEmpty() &&
            !textInputEmail.text.toString().isEmpty() &&
            !textPassword.text.toString().isEmpty()
        ) {

            requestRegisterPerson(Person(
                null, textInputName.text.toString(), textInputAddress.text.toString(), textInputNumber.text.toString(),
                textInputPhone.text.toString(), textInputCpf.text.toString(), textInputEmail.text.toString(),
                textPassword.text.toString()
            ))
        }
    }

    fun requestRegisterPerson(person: Person): Disposable {
        return ServiceRetrofitPet.requestRegisterPerson(person).subscribe({
            finish()
        }, { err ->
            showMessage(getString(R.string.error_register_person))
        })
    }

    private fun showMessage(message:String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
