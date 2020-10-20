package com.talentnest.everything.activities

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.talentnest.everything.model.UserOrder
import com.talentnest.everything.prevalent.Prevalent
import kotlinx.android.synthetic.main.activity_users_orders.*
import kotlinx.android.synthetic.main.order_item.view.*

class UsersOrdersActivity() : AppCompatActivity() {

    private lateinit var userOrderRef: DatabaseReference
    private lateinit var orderRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_orders)

        val sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE)
        val userPhone = sharedPreferences.getString("phone","")

        orderRef = FirebaseDatabase.getInstance().reference.child("Admin Purchase List")
        userOrderRef = FirebaseDatabase.getInstance().reference.child("User Purchase List").child("$userPhone")
        userOrderRecycler()

    }

    private fun userOrderRecycler() {

        val option = FirebaseRecyclerOptions.Builder<UserOrder>()
            .setQuery(userOrderRef, UserOrder::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<UserOrder, UserOrderViewHolder>(option) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOrderViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.order_item, parent, false)
                return UserOrderViewHolder(view)
            }


            override fun onBindViewHolder(
                holder: UserOrderViewHolder,
                position: Int,
                model: UserOrder
            ) {
                holder.itemView.customer_name.text = model.cName
                holder.itemView.customer_phone.text = model.cPhone
                holder.itemView.customer_address.text = model.cAddress
                holder.itemView.total_price.text = model.pPrice + " Kyats"
                holder.itemView.date.text = model.purDate
                holder.itemView.productId.text = model.productName
                holder.itemView.setOnClickListener {
                    showDialog(model.purId)
                }
            }

        }
        user_recycler.adapter = adapter
        user_recycler.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

    }

    private fun showDialog(purId: String?) {
        val list = arrayOf("Yes","No")
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Do you want to remove order?")
        alertDialog.setItems(list,DialogInterface.OnClickListener { dialogInterface, i ->
            when (i){
                0 -> {
                    userOrderRef.child(purId!!).removeValue()
                    orderRef.child(purId!!).removeValue()
                }
            }
        })
        alertDialog.show()
    }
}

    class UserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }
