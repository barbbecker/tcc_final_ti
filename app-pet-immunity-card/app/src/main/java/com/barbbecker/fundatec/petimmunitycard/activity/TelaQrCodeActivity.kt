package com.barbbecker.fundatec.petimmunitycard.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.barbbecker.fundatec.petimmunitycard.R
import com.barbbecker.fundatec.petimmunitycard.helper.QRCodeHelper
import com.barbbecker.fundatec.petimmunitycard.service.models.Pet
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.android.synthetic.main.activity_tela_qr_code.fab
import kotlinx.android.synthetic.main.activity_tela_qr_code.toolbar
import kotlinx.android.synthetic.main.content_tela_qr_code.*


class TelaQrCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_qr_code)
        setSupportActionBar(toolbar)

        val namePet = intent.getSerializableExtra("key_pet") as Pet

        fab.setOnClickListener { view ->
            startActivity(Intent(baseContext, TelaInicialActivity::class.java))
        }

        val bitmap = QRCodeHelper
            .newInstance(this)
            .setContent(namePet.name)
            .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
            .setMargin(2)
            .qrcOde
        imageQrCode.setImageBitmap(bitmap)
    }
}
