package com.example.collectioncloset_2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.view.Gravity
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class HomePage : AppCompatActivity(){

    private lateinit var btnAddCategory: MaterialButton
    private lateinit var tvCloseApp: TextView

    private lateinit var sqlLiteHelperHelper: SQLLiteHelperHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        sqlLiteHelperHelper = SQLLiteHelperHelper(this)
        initView()

        btnAddCategory.setOnClickListener{
            val intent = Intent(this, AddCategory::class.java)
            startActivity(intent)
        }

        val username = getUsernameFromSharedPreferences().toString()

        tvCloseApp.setOnClickListener{
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        val username = getUsernameFromSharedPreferences().toString()
        loadCategories(username)
    }

    private fun getUsernameFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    private fun initView() {
        btnAddCategory = findViewById(R.id.btn_addCategory)
        tvCloseApp = findViewById(R.id.tv_CloseApp)
    }

    private fun getCategoryGoalFromDatabase(username:String, categoryName: String): String{

        val categoryGoal = sqlLiteHelperHelper.getGoalForItem(username,categoryName)

        return categoryGoal

    }

    private fun loadCategories(username: String) {
        val categoryList = sqlLiteHelperHelper.getCategoriesForUser(username)
        val linearLayout = findViewById<LinearLayout>(R.id.ll_categories)
        linearLayout.removeAllViews()

        for (category in categoryList) {
            val categoryName = category
            val categoryGoal = getCategoryGoalFromDatabase(username,categoryName)

            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.closet_icon)

            imageView.setOnClickListener {

                val intent = Intent(this, ClosetItem::class.java)
                intent.putExtra("categoryName", categoryName)
                intent.putExtra("categoryGoal", categoryGoal)
                startActivity(intent)

                //Toast.makeText(this, "$categoryName clicked!", Toast.LENGTH_SHORT).show()
            }


            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.image_width),
                resources.getDimensionPixelSize(R.dimen.image_height)
            )
            layoutParams.gravity = Gravity.CENTER

            linearLayout.addView(imageView, layoutParams)

            val textView = TextView(this)
            textView.text = categoryName
            textView.textSize = 16f
            textView.setTextColor(Color.BLACK)
            textView.isAllCaps = true
            textView.gravity = Gravity.CENTER

            val textParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.text_width),
                resources.getDimensionPixelSize(R.dimen.text_height)
            )
            textParams.gravity = Gravity.CENTER

            linearLayout.addView(textView, textParams)
        }
    }

}