package com.barbbecker.fundatec.petimmunitycard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.barbbecker.fundatec.petimmunitycard.R
import com.barbbecker.fundatec.petimmunitycard.service.models.Vaccine
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_items_vaccines.view.*

class CustomAdapterVaccine(var context: Context, var apiVaccine: List<Vaccine>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CustomAdapterVaccine.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_items_vaccines, parent, false)
        return ViewHolder(v, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(apiVaccine[position])
    }


    override fun getItemCount(): Int {
        return apiVaccine.size
    }

    class ViewHolder(itemView: View, var ctx: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: Vaccine) {
            itemView.textNameVaccine.text = item.name
            if (item.linkVaccine.isNotEmpty()) {
                Picasso.get().load("https://pet-immunity-card-vaccine.s3.amazonaws.com/" + item.linkVaccine).fit().centerCrop().into(itemView.imageRotulo)
            }
            itemView.textDateApplic.text = item.dateApplication
            itemView.textDateReapplic.text = item.dateReapplication
        }
    }
}