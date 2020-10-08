package com.talentnest.everything.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.talentnest.everything.R
import com.talentnest.everything.model.Product
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_news_feed.*
import kotlinx.android.synthetic.main.product_items.view.*

class NewsFeedActivity : AppCompatActivity() {

    private lateinit var databaseRef: DatabaseReference
    private var lastBackClick = 0L
    private var difference = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_feed)

        databaseRef = FirebaseDatabase.getInstance().reference.child("products")
        logRecycleView()

    }

    private fun logRecycleView() {
        val options = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(databaseRef, Product::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<Product, ProductNewFeedViewHolder>(options) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ProductNewFeedViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_items, parent, false)
                return ProductNewFeedViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: ProductNewFeedViewHolder,
                position: Int,
                model: Product
            ) {
                holder.itemView.product_name.text = model.productName
                holder.itemView.product_description.text = model.productDescription
                holder.itemView.product_price.text = "${model.productPrice} Kyats"
                Glide.with(this@NewsFeedActivity).load(model.productImage)
                    .into(holder.itemView.product_image)

                holder.itemView.setOnClickListener {
                    val intent = Intent(this@NewsFeedActivity, ProductDetailsActivity::class.java)
                    intent.putExtra("pid", model.productID)
                    intent.putExtra("pName", model.productName)
                    startActivity(intent)
                }
            }
        }
        rcy_new_feed.adapter = adapter
        rcy_new_feed.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun onBackPressed() {

        val now = System.currentTimeMillis()
        if(now - lastBackClick > difference) {
            lastBackClick = now
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
            return
        }else super.onBackPressed()

    }
}

class ProductNewFeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

}

