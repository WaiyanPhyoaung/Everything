package com.talentnest.everything.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.talentnest.everything.R
import kotlinx.android.synthetic.main.activity_admin_category.*

class AdminCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_category)

        admin_logout.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        check_order.setOnClickListener {
            val intent = Intent(this, CheckOrderActivity::class.java)
            startActivity(intent)
        }

        tshirts.setOnClickListener {
            val intent = goIntent("tshirts")
            startActivity(intent)
        }
        sports.setOnClickListener {
            val intent = goIntent("sports")
            startActivity(intent)
        }
        female_dresses.setOnClickListener {
            val intent = goIntent("female_dresses")
            startActivity(intent)
        }
        sweather.setOnClickListener {
            val intent = goIntent("sweather")
            startActivity(intent)
        }
        head_phones.setOnClickListener {
            val intent = goIntent("head_phones")
            startActivity(intent)
        }
        purse_bags.setOnClickListener {
            val intent = goIntent("purse_bags")
            startActivity(intent)
        }
        hats.setOnClickListener {
            val intent = goIntent("hats")
            startActivity(intent)
        }
        shoes.setOnClickListener {
            val intent = goIntent("shoes")
            startActivity(intent)
        }
        glasses.setOnClickListener {
            val intent = goIntent("glasses")
            startActivity(intent)
        }
        laptops.setOnClickListener {
            val intent = goIntent("laptops")
            startActivity(intent)
        }
        watches.setOnClickListener {
            val intent = goIntent("watches")
            startActivity(intent)
        }
        mobiles.setOnClickListener {
           val intent = goIntent("mobiles")
            startActivity(intent)
        }
    }

    private fun goIntent(value: String): Intent {
        val intent = Intent(this, AdminAddNewProductActivity::class.java)
        intent.putExtra("Category", value)
        return intent
    }
}