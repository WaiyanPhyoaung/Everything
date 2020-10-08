package com.talentnest.everything.categoryAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talentnest.everything.R

import kotlinx.android.synthetic.main.admin_category_item.view.*


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.MyRecyclerViewHolder>() {

    inner class MyRecyclerViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_category_item,parent,false)
        return MyRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {
        holder.itemView.iv_category.setImageResource(Categories.getListOfCategory()[position].image)
    }

    override fun getItemCount(): Int {
        return Categories.getListOfCategory().size
    }
}