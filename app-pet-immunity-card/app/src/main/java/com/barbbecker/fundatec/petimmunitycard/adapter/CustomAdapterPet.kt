package com.barbbecker.fundatec.petimmunitycard.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.barbbecker.fundatec.petimmunitycard.R
import com.barbbecker.fundatec.petimmunitycard.activity.TelaQrCodeActivity
import com.barbbecker.fundatec.petimmunitycard.activity.TelaVaccineActivity
import com.barbbecker.fundatec.petimmunitycard.service.models.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_items_pets.view.*
import kotlinx.android.synthetic.main.list_items_pets.view.textNamePet

class CustomAdapterPet(var context: Context, var apiPet: List<Pet>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CustomAdapterPet.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_items_pets, parent, false)
        return ViewHolder(v, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(apiPet[position])
    }


    override fun getItemCount(): Int {
        return apiPet.size
    }

    class ViewHolder(itemView: View, var ctx: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: Pet) {
            itemView.textNamePet.text = item.name
            if (item.linkImage.isNotEmpty()) {
                Picasso.get().load("https://pet-immunity-card.s3.amazonaws.com/" + item.linkImage).fit().centerCrop().into(itemView.imagePet)
            }

            itemView.llQrCode.setOnClickListener { view ->

                val intent = Intent(ctx, TelaQrCodeActivity::class.java)
                intent.putExtra("key_pet", item)
                ctx.startActivity( intent )
            }

            itemView.llVaccine.setOnClickListener { view ->
                val intent = Intent(ctx, TelaVaccineActivity::class.java)
                intent.putExtra("Pet", item)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                ctx.startActivity( intent )
            }
        }
    }
}