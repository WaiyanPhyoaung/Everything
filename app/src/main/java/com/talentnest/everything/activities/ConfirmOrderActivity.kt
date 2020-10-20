package com.talentnest.everything.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.talentnest.everything.R
import com.talentnest.everything.prevalent.Prevalent
import kotlinx.android.synthetic.main.activity_confirm_order.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ConfirmOrderActivity : AppCompatActivity() {

    private lateinit var customerName: String
    private lateinit var customerPhone: String
    private lateinit var customerAddress: String
    private lateinit var productID: String
    private lateinit var productName: String
    private lateinit var productPrice: String
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var userFirebaseRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)

        productID = intent.getStringExtra("pid").toString()
        productName = intent.getStringExtra("pName").toString()
        productPrice = intent.getStringExtra("pPrice").toString()
        product_to_be_purchased.text = productName

        btn_confirm.setOnClickListener {
            customerName = customer_name.text.toString()
            customerPhone = customer_phone.text.toString()
            customerAddress = customer_address.text.toString()

            if (customerName.isNullOrEmpty())
                Toast.makeText(this, "Please fill your name!", Toast.LENGTH_SHORT).show()
            else if (customerPhone.isNullOrEmpty())
                Toast.makeText(this, "Please fill contact phone number !", Toast.LENGTH_SHORT)
                    .show()
            else if (customerAddress.isNullOrEmpty())
                Toast.makeText(this, "Fill all the blank !", Toast.LENGTH_SHORT).show()
            else
                addToPurchaseList()
        }
    }

    private fun addToPurchaseList() {
        val savePurchaseDate: String
        val savePurchaseTime: String

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy")
        val timeFormat = SimpleDateFormat("HH:mm:ss a")
        savePurchaseDate = dateFormat.format(calendar.time)
        savePurchaseTime = timeFormat.format(calendar.time)

        firebaseRef = FirebaseDatabase.getInstance().reference.child("Admin Purchase List")
        val sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE)
        val userPhone = sharedPreferences.getString("phone","")
        userFirebaseRef = FirebaseDatabase.getInstance().reference.child("User Purchase List").child("$userPhone")
        val hashMap = HashMap<String, Any>()
        hashMap["pId"] = productID
        hashMap["cName"] = customerName
        hashMap["cPhone"] = customerPhone
        hashMap["cAddress"] = customerAddress
        hashMap["productName"] = productName
        hashMap["pPrice"] = productPrice
        hashMap["purDate"] = savePurchaseDate
        hashMap["purId"] = savePurchaseDate + savePurchaseTime


        firebaseRef.child(savePurchaseDate + savePurchaseTime)
            .updateChildren(hashMap).addOnSuccessListener {
                Toast.makeText(this, "Purchased Successfully..", Toast.LENGTH_LONG).show()
            }
        userFirebaseRef.child(savePurchaseDate + savePurchaseTime)
            .updateChildren(hashMap).addOnSuccessListener {
                Toast.makeText(this, "Updated Purchase List Successfully...", Toast.LENGTH_LONG).show()
                val intent = Intent(this, NewsFeedActivity::class.java)
                startActivity(intent)
                finish()
            }


    }
}