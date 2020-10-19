package com.talentnest.everything.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.talentnest.everything.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference

    private lateinit var loadingProgressBar: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        val userName = register_username.text.toString()
        val phoneNumber = register_phone_number.text.toString()
        val password = register_password_number.text.toString()

        if (userName.isNullOrEmpty()) Toast.makeText(
            this,
            "Please enter your name...",
            Toast.LENGTH_SHORT
        ).show()
        else if (phoneNumber.isNullOrEmpty()) Toast.makeText(
            this,
            "Please enter your phone number...",
            Toast.LENGTH_SHORT
        ).show()
        else if (password.isNullOrEmpty()) Toast.makeText(
            this,
            "Please enter your password...",
            Toast.LENGTH_SHORT
        ).show()
        else {
            loadingProgressBar = AlertDialog.Builder(this).setTitle("Creating Account...")
                .setMessage("Please wait when we are checking the credentials")
                .create()

            loadingProgressBar.show()

            validatePhoneNumber(userName, phoneNumber, password)
        }
    }

    private fun validatePhoneNumber(userName: String, phoneNumber: String, password: String) {

        reference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!(snapshot.child("Users").child(phoneNumber).exists())) {
                    val dataHashMap = HashMap<String, Any>()
                    dataHashMap["userName"] = userName
                    dataHashMap.put("phoneNumber", phoneNumber)
                    dataHashMap.put("password", password)


                    reference.child("Users").child(phoneNumber)
                        .updateChildren(dataHashMap).addOnCompleteListener {

                            if (it.isSuccessful) {
                                loadingProgressBar.dismiss()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Congratulation, your account has been created",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                loadingProgressBar.dismiss()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Network Error! Please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    loadingProgressBar.dismiss()
                    Toast.makeText(
                        this@RegisterActivity,
                        "This $phoneNumber is already existed",
                        Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}
