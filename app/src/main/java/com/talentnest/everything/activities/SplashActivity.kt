package com.talentnest.everything.activities


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.talentnest.everything.prevalent.Prevalent

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE)
        val loggedIn = sharedPreferences.getString("admin", "")
        val loggedInUser = sharedPreferences.getString("user", "")

        if (loggedIn.isNullOrBlank()) {
            if (loggedInUser.isNullOrBlank()) {
                val intent = Intent(this, MainActivity::class.java)
                startIntent(intent)

            } else if (loggedInUser.isNotEmpty()){
                val intent = Intent(this,NewsFeedActivity::class.java)
                startIntent(intent)
            }

        }else{
            val intent = Intent(this,AdminCategoryActivity::class.java)
            startIntent(intent)
        }
    }
        private fun startIntent(intent: Intent){
            startActivity(intent)
            finish()
        }

}






