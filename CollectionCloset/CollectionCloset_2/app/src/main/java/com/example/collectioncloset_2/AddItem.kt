package com.example.collectioncloset_2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.button.MaterialButton
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddItem : AppCompatActivity() {

    private lateinit var edName: EditText
    private lateinit var edDescription: EditText
    private lateinit var edDate: EditText
    private lateinit var btnAddItem2 : MaterialButton
    private lateinit var btnPicture : MaterialButton

    private lateinit var sqlLiteHelperHelper: SQLLiteHelperHelper

    private var imageBitmap: Bitmap? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val takenImage = result.data?.extras?.get("data") as Bitmap
                imageBitmap = takenImage
                val username = getUsernameFromSharedPreferences().toString()
                val itemName = edName.text.toString()
                val pictureFileName = generatePictureFileName(username, itemName)
                val picturePath = generatePicturePath(username, itemName)
                saveBitmapToStorage(imageBitmap!!, picturePath)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        sqlLiteHelperHelper = SQLLiteHelperHelper(this)
        initView()

        btnPicture.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureLauncher.launch(takePictureIntent)
        }
    }

    override fun onResume() {
        super.onResume()

        val username = getUsernameFromSharedPreferences().toString()
        val categoryName = intent.getStringExtra("categoryName")
        val categoryGoal = intent.getStringExtra("categoryGoal")

        btnAddItem2.setOnClickListener{

            addItem(categoryName.toString(), categoryGoal.toString())
            val intent = Intent(this, ClosetItem::class.java)
            intent.putExtra("categoryName", categoryName)
            intent.putExtra("categoryGoal", categoryGoal)
            startActivity(intent)

        }
    }
    private fun addItem(categoryName: String, categoryGoal: String) {
        val itemName = edName.text.toString()
        val itemDescription = edDescription.text.toString()
        var itemDate = edDate.text.toString()
        val username = getUsernameFromSharedPreferences().toString()
        val goalInt = categoryGoal.toInt()

        if (itemDate.isEmpty()) {
            itemDate = "No Date Entered"
        }

        if (categoryName.isEmpty() || categoryGoal.isEmpty() || itemName.isEmpty() || itemDescription.isEmpty()) {
            Toast.makeText(this, "Enter the required fields.", Toast.LENGTH_SHORT).show()
        } else {
            val std = UserModel(
                username = username,
                category = categoryName,
                goal = goalInt,
                item_Name = itemName,
                item_Description = itemDescription,
                item_Date = itemDate,
                picture_Path = generatePictureFileName(username, itemName)
            )
            val status = sqlLiteHelperHelper.insertItem(std)

            Toast.makeText(this,generatePictureFileName(username, itemName), Toast.LENGTH_SHORT).show()

            if (status > -1) {
                Toast.makeText(this, "Item added ...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Item not added ...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveBitmapToStorage(bitmap: Bitmap, fileName: String) {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        try {
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun generatePicturePath(username: String, itemName: String): String {
        return username + "_" + itemName + ".jpg"
    }

    private fun generatePictureFileName(username: String, itemName: String): String {
        return username + "_" + itemName + ".jpg"
    }

    private fun getUsernameFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    private fun initView() {
        edName = findViewById(R.id.ed_ItemName)
        edDescription = findViewById(R.id.ed_ItemDescription)
        edDate = findViewById(R.id.ed_ItemDate)
        btnAddItem2 = findViewById(R.id.btn_addItem2)
        btnPicture = findViewById(R.id.btn_Picture)

    }
}