package com.example.collectioncloset_2

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.lang.Exception

class SQLLiteHelperHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "wardrobe.db"
        private const val DATABASE_VERSION = 7

        private const val TBL_WARDROBE = "tbl_wardrobe"
        private const val TBL_CATEGORY = "tbl_category"
        private const val TBL_ITEMS = "tbl_items"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
        private const val CATEGORY = "category"
        private const val GOAL = "goal"
        private const val ITEM_NAME = "item_name"
        private const val ITEM_DESCRIPTION = "item_description"
        private const val ITEM_DATE = "item_date"
        private const val PICTURE_PATH = "picture_Path"
    }


    override fun onCreate(db: SQLiteDatabase) {
        val createTblWardrobe =
            "CREATE TABLE $TBL_WARDROBE ($USERNAME TEXT, $PASSWORD TEXT)"
        db?.execSQL(createTblWardrobe)

        val createTblCategory =
            "CREATE TABLE $TBL_CATEGORY ($USERNAME TEXT, $CATEGORY TEXT, $GOAL INT)"
        db?.execSQL(createTblCategory)

        val createTblItems =
            "CREATE TABLE $TBL_ITEMS ($USERNAME TEXT, $CATEGORY TEXT, $GOAL INT, $ITEM_NAME TEXT, $ITEM_DESCRIPTION TEXT, $ITEM_DATE TEXT, $PICTURE_PATH TEXT)"
        db?.execSQL(createTblItems)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_WARDROBE")
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_CATEGORY")
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_ITEMS")
        onCreate(db)
    }

    fun insertUser(std: UserModel): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USERNAME, std.username)
        contentValues.put(PASSWORD, std.password)

        val success = db.insert(TBL_WARDROBE, null, contentValues)
        db.close()
        return success
    }

    fun insertCategory(std: UserModel): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USERNAME, std.username)
        contentValues.put(CATEGORY, std.category)
        contentValues.put(GOAL, std.goal)

        val success = db.insert(TBL_CATEGORY, null, contentValues)
        db.close()
        return success
    }

    fun insertItem(std: UserModel): Long {

        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(USERNAME, std.username)
        contentValues.put(CATEGORY, std.category)
        contentValues.put(GOAL, std.goal)
        contentValues.put(ITEM_NAME, std.item_Name)
        contentValues.put(ITEM_DESCRIPTION, std.item_Description)
        contentValues.put(ITEM_DATE, std.item_Date)
        contentValues.put(PICTURE_PATH, std.picture_Path)

        val success = db.insert(TBL_ITEMS, null, contentValues)
        db.close()
        return success
    }


    @SuppressLint("Range")
    fun getAllUser(): ArrayList<UserModel>{
        val stdList: ArrayList<UserModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_WARDROBE"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var username: String
        var password: String

        if(cursor.moveToFirst()){
            do{
                username = cursor.getString(cursor.getColumnIndex("username"))
                password = cursor.getString(cursor.getColumnIndex("password"))

                val std = UserModel(username = username, password = password)
                stdList.add(std)


            } while(cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return stdList
    }

    @SuppressLint("Range")
    fun getCategoriesForUser(username: String): ArrayList<String> {
        val categoryList: ArrayList<String> = ArrayList()
        val selectQuery = "SELECT category FROM $TBL_CATEGORY WHERE $USERNAME = '$username'"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                val category = cursor.getString(cursor.getColumnIndex("category"))
                categoryList.add(category)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return categoryList
    }

    @SuppressLint("Range")
    fun getItemsForUser(username: String, categoryName: String): ArrayList<String> {
        val itemList: ArrayList<String> = ArrayList()
        val selectQuery = "SELECT item_Name FROM $TBL_ITEMS WHERE $USERNAME = '$username' AND $CATEGORY = '$categoryName'"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                val itemName = cursor.getString(cursor.getColumnIndex(ITEM_NAME))
                itemList.add(itemName)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemList
    }


    @SuppressLint("Range")
    fun getGoalForItem(username: String, categoryName: String): String {
        var goal: String = ""

        val selectQuery =
            "SELECT goal FROM $TBL_CATEGORY WHERE $USERNAME = '$username' AND $CATEGORY = '$categoryName'"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                goal = cursor.getString(cursor.getColumnIndex("goal"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return goal
        } finally {
            cursor?.close()
            db.close()
        }

        return goal
    }

    @SuppressLint("Range")
    fun getDetailsForList(username: String, itemName: String): ArrayList<ArrayList<String>> {
        val itemDetails = ArrayList<ArrayList<String>>()
        val selectQuery = "SELECT item_Description, item_Date, picture_Path FROM $TBL_ITEMS WHERE $USERNAME = '$username' AND $ITEM_NAME = '$itemName'"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return itemDetails // Return empty ArrayList
        }

        if (cursor.moveToFirst()) {
            do {
                val itemDetailsRow = ArrayList<String>()
                val itemDescription = cursor.getString(cursor.getColumnIndex(ITEM_DESCRIPTION))
                val itemDate = cursor.getString(cursor.getColumnIndex(ITEM_DATE))
                val picturePath = cursor.getString(cursor.getColumnIndex(PICTURE_PATH))

                itemDetailsRow.add(itemDescription)
                itemDetailsRow.add(itemDate)
                itemDetailsRow.add(picturePath)

                itemDetails.add(itemDetailsRow)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemDetails
    }


    fun getItemCountForUser(username: String): Int {
        val countQuery = "SELECT COUNT(*) FROM $TBL_ITEMS WHERE $USERNAME = '$username'"
        val db = this.readableDatabase
        var count = 0

        val cursor: Cursor? = db.rawQuery(countQuery, null)
        cursor?.let {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
            it.close()
        }

        db.close()
        return count
    }

}