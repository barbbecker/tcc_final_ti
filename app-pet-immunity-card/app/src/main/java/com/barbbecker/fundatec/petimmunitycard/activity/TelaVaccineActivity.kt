package com.barbbecker.fundatec.petimmunitycard.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barbbecker.fundatec.petimmunitycard.R
import com.barbbecker.fundatec.petimmunitycard.adapter.CustomAdapterVaccine
import com.barbbecker.fundatec.petimmunitycard.network.ServiceRetrofitPet
import com.barbbecker.fundatec.petimmunitycard.service.models.Pet
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_tela_vaccine.*
import kotlinx.android.synthetic.main.content_tela_vaccine.*

class TelaVaccineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_vaccine)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            addNewVaccine()
        }
    }

    private fun addNewVaccine() {

        val pet = intent.getSerializableExtra("Pet") as Pet
        Log.e("pet", pet.toString())

        val intent = Intent(this, TelaAddVaccineActivity::class.java)
        intent.putExtra("pet_id", pet.id)
        startActivity( intent )
    }

    override fun onResume() {
        super.onResume()
        val pet = intent.getSerializableExtra("Pet") as Pet

        pet.id?.let { requestGetVaccines(it) }
    }

    private fun requestGetVaccines(id: Int): Disposable {
        return ServiceRetrofitPet.requestGetVaccines(id).subscribe({
            Log.e("Vaccines", it.toString())
            runOnUiThread {
                val customAdapter = CustomAdapterVaccine(baseContext, it)
                recyclerViewAllVaccines.apply {
                    layoutManager = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
                    adapter = customAdapter
                }
            }
        }, { err ->
            showMessage(getString(R.string.error_get_vaccines))
        })
    }

    private fun showMessage(message:String) {
        runOnUiThread {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
