package com.talentnest.everything.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.talentnest.everything.R
import com.talentnest.everything.model.Product
import kotlinx.android.synthetic.main.activity_product_details.*


class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productID: String
    private lateinit var productRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        productID = intent.getStringExtra("pid").toString()
        getProductDetails(productID)
    }

    private fun getProductDetails(productID: String) {

        productRef = FirebaseDatabase.getInstance().reference.child("products")
        productRef.child(productID).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val productOne = snapshot.getValue(Product::class.java)
                    product_name.text = productOne!!.productName
                    product_price.text = productOne!!.productPrice
                    product_description.text = productOne!!.productDescription
                    Glide.with(this@ProductDetailsActivity).load(productOne.productImage)
                        .into(product_image)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}