package com.talentnest.everything.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.talentnest.everything.R
import com.talentnest.everything.model.Product
import com.talentnest.everything.prevalent.Prevalent
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.product_items.view.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var databaseRef: DatabaseReference
    private lateinit var exitDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.title = "Menu"


        val drawerToggle = ActionBarDrawerToggle(
            this, drawer_layout, toolBar, (R.string.open), (R.string.close)
        )
        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        // d mhr yay mha ya ... apor mhr yay yin crash
        val navigationView = nav_view
        val headerView = navigationView.getHeaderView(0)
        val userProfileName = headerView.name_of_user

        userProfileName.text = Prevalent.currentOnlineUser.userName


        //    val homeFragment = HomeFragment()
        //  supportFragmentManager.beginTransaction().replace(R.id.frameLayout,homeFragment).commit()
        databaseRef = FirebaseDatabase.getInstance().reference.child("products")
        logRecycleView()


    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {

                exitDialog = AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                    .setMessage("Are you sure, you want to exit?")
                    .setPositiveButton("Yes") { _, _ ->
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        finish()
                        startActivity(intent)
                    }
                    .setNegativeButton("No") { dialogInterface, Int ->
                        exitDialog.cancel()
                    }
                    .create()
                exitDialog.show()


            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun logRecycleView() {
        val options = FirebaseRecyclerOptions.Builder<Product>()
            .setQuery(databaseRef, Product::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_items, parent, false)
                return ProductViewHolder(view)
            }

            override fun onBindViewHolder(
                holder: ProductViewHolder,
                position: Int,
                model: Product
            ) {
                holder.itemView.product_name.text = model.productName
                holder.itemView.product_description.text = model.productDescription
                holder.itemView.product_price.text = model.productPrice
            }

        }
        rcy_home.adapter = adapter
        rcy_home.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
    }


}


class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}




