package com.example.collectioncloset_2

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class ClosetItem : AppCompatActivity() {

    private lateinit var btnaddItem: MaterialButton
    private lateinit var tvItem: TextView
    private lateinit var tvGoal: TextView
    private lateinit var tvItemNumber: TextView
    private lateinit var tvHomePage: TextView

    private lateinit var sqlLiteHelperHelper: SQLLiteHelperHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet_item)

        sqlLiteHelperHelper = SQLLiteHelperHelper(this)
        initView()

        val username = getUsernameFromSharedPreferences().toString()
        val categoryName = intent.getStringExtra("categoryName")
        val categoryGoal = intent.getStringExtra("categoryGoal")

        tvItem.text = categoryName
        tvItem.isAllCaps = true

        tvGoal.text = "GOAL: $categoryGoal"

        btnaddItem.setOnClickListener{
            val intent = Intent(this, AddItem::class.java)
            intent.putExtra("categoryName", categoryName)
            intent.putExtra("categoryGoal", categoryGoal)
            startActivity(intent)
        }

        tvHomePage.setOnClickListener{
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

    }
    private var milestonesAchieved: MutableSet<Int> = mutableSetOf()

    override fun onResume() {
        super.onResume()

        val username = getUsernameFromSharedPreferences().toString()
        val categoryGoal = intent.getStringExtra("categoryGoal").toString()
        val categoryName = intent.getStringExtra("categoryName").toString()
        loadItems(username, categoryName, categoryGoal)

        trackMilestones(username)
    }

    private fun trackMilestones(username: String) {

        val itemCount = sqlLiteHelperHelper.getItemCountForUser(username)

        when (itemCount) {
            1 -> {
                if (!milestonesAchieved.contains(1)) {
                    Toast.makeText(this, "Congratulations! You have added your first item.", Toast.LENGTH_LONG).show()
                    milestonesAchieved.add(1)
                }
            }
            3 -> {
                if (!milestonesAchieved.contains(3)) {
                    Toast.makeText(this, "Congratulations! You have added 3 items in total.", Toast.LENGTH_LONG).show()
                    milestonesAchieved.add(3)
                }
            }
            10 -> {
                if (!milestonesAchieved.contains(10)) {
                    Toast.makeText(this, "Congratulations! You have added 10 items in total.", Toast.LENGTH_LONG).show()
                    milestonesAchieved.add(10)
                }
            }
        }
    }

    private fun loadItems(username: String, categoryName: String, categoryGoal: String){

        val itemList = sqlLiteHelperHelper.getItemsForUser(username, categoryName)
        val goal = categoryGoal.toInt()
        val linearLayout = findViewById<LinearLayout>(R.id.ll_items)
        linearLayout.removeAllViews()

        for (item in itemList) {
            val itemName = item

            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.hanger)

            imageView.setOnClickListener {

                val intent = Intent(this, ItemDetails::class.java)
                intent.putExtra("itemName", itemName)
                startActivity(intent)
            }

            val layoutParams = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.image_width),
                resources.getDimensionPixelSize(R.dimen.image_height)
            )
            layoutParams.gravity = Gravity.CENTER

            linearLayout.addView(imageView, layoutParams)

            val textView = TextView(this)
            textView.text = itemName
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

            val itemCount = itemList.size
            tvItemNumber.text = "ITEMS: $itemCount"

            if (itemCount == goal)
            {
                Toast.makeText(this, "CONGRATULATIONS! You have reached your goal in this category!!", Toast.LENGTH_LONG).show()
            }else if (itemCount > goal)
            {
                Toast.makeText(this, "CONGRATULATIONS! You have surpassed your goal in this category!!", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun getUsernameFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }

    private fun initView() {
        btnaddItem = findViewById(R.id.btn_addItem)
        tvItem = findViewById(R.id.tv_Item)
        tvGoal = findViewById(R.id.tv_Goal)
        tvItemNumber = findViewById(R.id.tv_itemNumber)
        tvHomePage = findViewById(R.id.tv_HomePage)

    }
}