package com.barbbecker.fundatec.petimmunitycard.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barbbecker.fundatec.petimmunitycard.R
import com.barbbecker.fundatec.petimmunitycard.adapter.CustomAdapterPet
import com.barbbecker.fundatec.petimmunitycard.network.ServiceRetrofitPet
import com.barbbecker.fundatec.petimmunitycard.service.models.Session
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_tela_inicial.*
import kotlinx.android.synthetic.main.content_tela_inicial.*


class TelaInicialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_inicial)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            addNewPet()
        }
    }

    override fun onResume() {
        super.onResume()
        Session.personId?.let { requestGetPets(it) }
    }

    private fun addNewPet() {
        startActivity(Intent(baseContext, TelaCadastroActivity::class.java))
    }

    fun requestGetPets(personId: Int): Disposable {
        return ServiceRetrofitPet.requestGetPets(personId).subscribe({
            Log.e("Pets", it.toString())
            runOnUiThread {
                val customAdapter = CustomAdapterPet(baseContext, it)
                recyclerViewInitialScreen.apply {
                    layoutManager = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
                    adapter = customAdapter
                }
            }
        }, { err ->
            showMessage(getString(R.string.error_get_pets))
        })
    }

    private fun showMessage(message:String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
