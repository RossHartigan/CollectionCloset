package com.example.collectioncloset_2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.io.File


class ItemDetails : AppCompatActivity() {

    private lateinit var ivItem: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvClosetPage: TextView

    private lateinit var sqlLiteHelperHelper: SQLLiteHelperHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        sqlLiteHelperHelper = SQLLiteHelperHelper(this)
        initView()

        tvClosetPage.setOnClickListener{
            val intent = Intent(this, ClosetItem::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val username = getUsernameFromSharedPreferences().toString()
        val itemName = intent.getStringExtra("itemName").toString()
        loadDetails(username, itemName)
    }

    private fun loadDetails(username: String, itemName: String) {
        val itemDetails = sqlLiteHelperHelper.getDetailsForList(username, itemName)

        val itemDescription = itemDetails[0][0]
        val itemDate = itemDetails[0][1]
        val picturePath = itemDetails[0][2]

        Toast.makeText(this,picturePath, Toast.LENGTH_SHORT).show()

        tvName.text = itemName
        tvName.isAllCaps = true
        tvName.gravity = Gravity.CENTER

        tvDescription.text = itemDescription
        tvDescription.gravity = Gravity.CENTER

        tvDate.text = itemDate
        tvDate.gravity = Gravity.CENTER
        Glide.with(this)
            .load(File(picturePath))
            .into(ivItem)

    }


    private fun getUsernameFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    private fun initView() {
        ivItem = findViewById(R.id.iv_Item)
        tvName = findViewById(R.id.tv_Name)
        tvDescription = findViewById(R.id.tv_Description)
        tvDate = findViewById(R.id.tv_Date)
        tvClosetPage = findViewById(R.id.tv_ClosetPage)

    }
}