package com.talentnest.everything.activities

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.talentnest.everything.R
import com.talentnest.everything.model.AdminOrder
import kotlinx.android.synthetic.main.activity_check_order.*
import kotlinx.android.synthetic.main.order_item.view.*

class CheckOrderActivity : AppCompatActivity() {

    private lateinit var orderRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_order)

        orderRef = FirebaseDatabase.getInstance().reference.child("Admin Purchase List")
        orderRecycler()
    }

    private fun orderRecycler() {

        val option = FirebaseRecyclerOptions.Builder<AdminOrder>()
            .setQuery(orderRef, AdminOrder::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<AdminOrder, OrderViewHolder>(option) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.order_item, parent, false)
                return OrderViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: OrderViewHolder,
                position: Int,
                model: AdminOrder
            ) {
                holder.itemView.customer_name.text = model.cName
                holder.itemView.customer_phone.text = model.cPhone
                holder.itemView.customer_address.text = model.cAddress
                holder.itemView.total_price.text = model.pPrice + " Kyats"
                holder.itemView.date.text = model.purDate
                holder.itemView.productId.text = model.productName
                holder.itemView.setOnClickListener {
                    showOption(model.purId)
                }

            }

        }
        order_recycler.adapter = adapter
        order_recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }

    private fun showOption(purId : String?) {
        val itemList = arrayOf("Yes","No")
      val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Have you delivered this product?")
        alertDialog.setItems(itemList,DialogInterface.OnClickListener { dialogInterface, i ->
            if(i == 0){
                orderRef.child(purId!!).removeValue()
            }else finish()
        })
        alertDialog.show()

    }
}

class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}