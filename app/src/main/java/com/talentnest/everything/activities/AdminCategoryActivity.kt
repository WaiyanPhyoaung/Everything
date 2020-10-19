package com.talentnest.everything.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.talentnest.everything.R
import com.talentnest.everything.categoryAdapter.Category
import com.talentnest.everything.categoryAdapter.RecyclerViewAdapter
import com.talentnest.everything.listener.AdminCategoryListener
import kotlinx.android.synthetic.main.activity_admin_category.*


class AdminCategoryActivity : AppCompatActivity(), AdminCategoryListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_category)

        admin_logout.setOnClickListener {
            val sharedPref = getSharedPreferences("MyData", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.commit()
            val intent = Intent(this,LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        check_order.setOnClickListener {
            val intent = Intent(this, CheckOrderActivity::class.java)
            startActivity(intent)
        }

        recycler_category.layoutManager =
            GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        recycler_category.adapter = RecyclerViewAdapter(this)

    }

    private fun goIntent(value: String): Intent {
        val intent = Intent(this, AdminAddNewProductActivity::class.java)
        intent.putExtra("Category", value)
        return intent
    }

    override fun onItemClicked(data: Category) {
        val intent = goIntent(data.name)
        startActivity(intent)
    }
}
