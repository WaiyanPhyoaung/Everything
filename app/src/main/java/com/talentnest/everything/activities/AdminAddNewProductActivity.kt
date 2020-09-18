package com.talentnest.everything.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.talentnest.everything.R
import kotlinx.android.synthetic.main.activity_admin_add_new_product.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AdminAddNewProductActivity : AppCompatActivity() {

    private lateinit var loadingDialog: AlertDialog
    private lateinit var productName: String
    private lateinit var productDescription: String
    private lateinit var productPrice: String
    private val galleryPick = 1
    private var imageUri: Uri? = null
    private lateinit var categoryName: String
    private lateinit var saveCurrentDate: String
    private lateinit var saveCurrentTime: String
    private lateinit var productImageRef: StorageReference
    private lateinit var productRandomKey: String
    private lateinit var filePath: StorageReference
    private lateinit var productsReference: DatabaseReference
    private lateinit var downloadImageUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_new_product)


        categoryName = intent.getStringExtra("Category").toString()
        Toast.makeText(this, categoryName, Toast.LENGTH_LONG).show()

        productImageRef = FirebaseStorage.getInstance().reference.child("product images")
        productsReference = FirebaseDatabase.getInstance().reference.child("products")

        select_product_photo.setOnClickListener {
            galleryOpen()
        }

        add_product_button.setOnClickListener {
            productName = product_name.text.toString()
            productDescription = product_description.text.toString()
            productPrice = product_price.text.toString()

            if (imageUri == null) Toast.makeText(
                this, "Product image is mandatory",
                Toast.LENGTH_SHORT
            )
                .show()
            else if (productName.isNullOrEmpty() || productDescription.isNullOrEmpty() || productPrice.isNullOrEmpty())
                Toast.makeText(
                    this, "Please give about the products",
                    Toast.LENGTH_SHORT
                ).show()
            else storeProductInformation()

        }
    }

    private fun storeProductInformation() {

        showLoadingDialog(this, "Please wait, adding new product..")
        val calendar = Calendar.getInstance().time
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = simpleDateFormat.format(calendar)

        val simpleTimeFormat = SimpleDateFormat("HH:mm::ss a")
        saveCurrentTime = simpleTimeFormat.format(calendar)

        productRandomKey = saveCurrentDate + saveCurrentTime
        Log.i("product", productRandomKey)

        filePath =
            productImageRef.child(imageUri!!.lastPathSegment.toString() + productRandomKey + ".jpg")

        val uploadTask = filePath.putFile(imageUri!!)
        uploadTask.addOnFailureListener {
            hideDialog()
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {

            Toast.makeText(this, "Uploaded photo successfully...", Toast.LENGTH_SHORT).show()
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                return@Continuation filePath.downloadUrl

            }).addOnCompleteListener {
                if (it.isSuccessful)
                    downloadImageUrl = it.result.toString()
                    Toast.makeText(
                        this,
                        "Got product image Url successfully..",
                        Toast.LENGTH_SHORT
                    ).show()
                saveProductInfoToDatabase()
            }
        }
    }

    private fun saveProductInfoToDatabase() {
        val productMap = HashMap<String, Any>()
        productMap.put("productID", productRandomKey)
        productMap["productDate"] = saveCurrentDate
        productMap["productTime"] = saveCurrentTime
        productMap["productImage"] = downloadImageUrl
        productMap["productCategory"] = categoryName
        productMap["productName"] = productName
        productMap.put("productDescription", productDescription)
        productMap.put("productPrice", productPrice)

        productsReference.child(productRandomKey).updateChildren(productMap).addOnCompleteListener {
            if (it.isSuccessful) {
                hideDialog()
                Toast.makeText(this, "Product is added successfully...", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminCategoryActivity::class.java)
                startActivity(intent)
            } else {
                hideDialog()
                val string = it.exception.toString()
                Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun galleryOpen() {
        val galleryIntent = Intent().setAction(Intent.ACTION_GET_CONTENT)
            .setType("image/*")
        startActivityForResult(galleryIntent, galleryPick)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData()
            select_product_photo.setImageURI(imageUri)

        }
    }

    private fun showLoadingDialog(context: Context, message: String) {
        loadingDialog = AlertDialog.Builder(context)
            .setTitle("Loading...")
            .setMessage(message)
            .create()
        loadingDialog.show()
    }

    private fun hideDialog() {
        loadingDialog.dismiss()
    }
}