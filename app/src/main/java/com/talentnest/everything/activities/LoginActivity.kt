package com.talentnest.everything.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.talentnest.everything.R
import com.talentnest.everything.model.UserModel
import com.talentnest.everything.prevalent.Prevalent



import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loadingDialog: AlertDialog
    private var parentDbName = "Users"

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        admin_panel_link.setOnClickListener {
            main_login_button.text = "Login Admin"
            admin_panel_link.visibility = View.INVISIBLE
            not_admin_panel_link.visibility = View.VISIBLE
            parentDbName = "Admins"
        }
        not_admin_panel_link.setOnClickListener {
            main_login_button.text = "Login"
            admin_panel_link.visibility = View.VISIBLE
            not_admin_panel_link.visibility = View.INVISIBLE
            parentDbName = "Users"
        }


        main_login_button.setOnClickListener {
            val loginPhoneNumber = login_phone_number.text.toString()
            val loginPassword = login_password_number.text.toString()


            if (loginPhoneNumber.isNullOrEmpty())
                Toast.makeText(this, "Enter phone number...", Toast.LENGTH_SHORT).show()
            else if (loginPassword.isNullOrEmpty())
                Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show()
            else {
                showLoadingDialog(this, "Please wait while we are checking credentials")
                allowAccessToLogIn(loginPhoneNumber, loginPassword)
            }
        }
    }

    private fun allowAccessToLogIn(loginPhoneNumber: String, loginPassword: String) {

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(parentDbName).child(loginPhoneNumber).exists()) {


                    val user: UserModel? = snapshot.child(parentDbName).child(loginPhoneNumber)
                        .getValue(UserModel::class.java)

                    Log.i("MOOO", "${user?.phoneNumber}${user?.password}${user?.userName}")
                    if (user?.phoneNumber.equals(loginPhoneNumber)) {
                        if (user?.password.equals(loginPassword)) {


                            if (parentDbName == "Admins") {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Admin Logged in successfully...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                hideDialog()


                                val sharedPreferences = getSharedPreferences("MyData",Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("admin","admin").commit()

                                val intent =
                                    Intent(this@LoginActivity, AdminCategoryActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)

                            } else if (parentDbName == "Users") {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                hideDialog()

                                val sharedPreferences = getSharedPreferences("MyData",Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("user","user")
                                editor.putString("phone",user!!.phoneNumber)
                                editor.commit()


                                val intent =
                                    Intent(this@LoginActivity, NewsFeedActivity::class.java)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                Prevalent.currentOnlineUser = user!!
                                startActivity(intent)

                            }
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Wrong password...",
                                Toast.LENGTH_SHORT
                            ).show()
                            hideDialog()
                        }
                    }


                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "This account doesn't exist...",
                        Toast.LENGTH_SHORT
                    ).show()
                    hideDialog()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Error", error.details)
            }

        })
    }

    private fun showLoadingDialog(context: Context, message: String) {
        loadingDialog = AlertDialog.Builder(context)
            .setTitle("Loading...")
            .setMessage(message)
            .create()
        loadingDialog.show()
    }

    fun hideDialog() {
        loadingDialog.dismiss()
    }
}
