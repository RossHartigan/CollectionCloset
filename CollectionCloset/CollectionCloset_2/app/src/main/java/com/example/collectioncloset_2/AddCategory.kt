package com.example.collectioncloset_2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class AddCategory : AppCompatActivity() {

    private lateinit var btnAddCategory: MaterialButton
    private lateinit var edCategory: EditText
    private lateinit var edGoal: EditText

    private lateinit var sqlLiteHelperHelper: SQLLiteHelperHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        sqlLiteHelperHelper = SQLLiteHelperHelper(this)
        initView()

        btnAddCategory.setOnClickListener{
            (addCategory())
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

    }

    private fun getUsernameFromSharedPreferences(): String? {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", null)
    }


    private fun addCategory(){

        val category = edCategory.text.toString()
        val goal = edGoal.text.toString()
        val username = getUsernameFromSharedPreferences().toString()
        val goalInt = goal.toInt()

        if (category.isEmpty() || goal.isEmpty()) {
            Toast.makeText(this, "Enter the required fields.", Toast.LENGTH_SHORT).show()
        } else {
            val std = UserModel(username = username ,category = category,goal = goalInt)
            val status = sqlLiteHelperHelper.insertCategory(std)

            if (status > -1){
                Toast.makeText(this,"Category added ...", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Category not added ...", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun initView() {
        btnAddCategory = findViewById(R.id.btn_addCategory2)
        edCategory = findViewById(R.id.ed_CategoryName)
        edGoal = findViewById(R.id.ed_categoryGoal)

    }
}