package com.test.archi.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.test.archi.utils.DLog
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by User on 06-May-17.
 */
class DB @Inject constructor(@ApplicationContext context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val db: SQLiteDatabase? = null

    @Inject
    lateinit var dLog: DLog

    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE" + " " + TABLE_ADDTOCART + "("
                + KEY_DATA_ADDTOCART + " INTEGER"
                + ")")
        db.execSQL(CREATE_CONTACTS_TABLE)
        val CREATE_WISHLIST_TABLE = ("CREATE TABLE" + " " + TABLE_WISHLIST + "("
                + KEY_DATA_WISHLIST + " INTEGER"
                + ")")
        db.execSQL(CREATE_WISHLIST_TABLE)
        val CREATE_ADDTOCART_CUST_TABLE = ("CREATE TABLE" + " " + TABLE_ADDTOCART_CUST + "("
                + KEY_DATA_ADDTOCART_CUST + " INTEGER"
                + ")")
        db.execSQL(CREATE_ADDTOCART_CUST_TABLE)
        val CREATE_WISHLIST_CUST_TABLE = ("CREATE TABLE" + " " + TABLE_WISHLISTZ_CUST + "("
                + KEY_DATA_WISHLIST_CUST + " INTEGER"
                + ")")
        db.execSQL(CREATE_WISHLIST_CUST_TABLE)
        val CREATE_LOGIN_DETAILS = ("CREATE TABLE" + " " + LOGIN_DETAILS + "("
                + KEY_DATA_LOGIN + " INTEGER"
                + ")")
        db.execSQL(CREATE_LOGIN_DETAILS)
        val CREATE_LOGIN_ROLE = ("CREATE TABLE" + " " + LOGIN_ROLE + "("
                + KEY_DATA_ROLE + " INTEGER"
                + ")")
        db.execSQL(CREATE_LOGIN_ROLE)
        val CREATE_LOGIN_COOKIE = ("CREATE TABLE" + " " + LOGIN_COOKIE + "("
                + KEY_DATA_COOKIE + " INTEGER"
                + ")")
        db.execSQL(CREATE_LOGIN_COOKIE)
        val CREATE_LOGIN_TOKEN = ("CREATE TABLE" + " " + DEVICE_TOKEN + "("
                + KEY_DATA_TOKEN + " INTEGER"
                + ")")
        db.execSQL(CREATE_LOGIN_TOKEN)
        val LANGUAGE_SELECTION = ("CREATE TABLE" + " " + TABLE_LANGUAGE_SELECTION + "("
                + KEY_DATA_LANG + " INTEGER"
                + ")")
        db.execSQL(LANGUAGE_SELECTION)
        val LOGIN_TYPE = ("CREATE TABLE" + " " + TABLE_LOGIN_TYPE + "("
                + KEY_LOGIN_TYPE + " INTEGER"
                + ")")
        db.execSQL(LOGIN_TYPE)
        val CREATE_TERMS = ("CREATE TABLE" + " " + TABLE_TERMS + "("
                + KEY_LOGIN_TERMS + " INTEGER"
                + ")")
        db.execSQL(CREATE_TERMS)
        val CREATE_REFRESH_TOKEN = ("CREATE TABLE IF NOT EXISTS" + " " + TABLE_REFRESH_TOKEN + "("
                + KEY_REFRESH_TOKEN + " INTEGER"
                + ")")
        db.execSQL(CREATE_REFRESH_TOKEN)
        dLog.i("onCreate", "onCreate: $CREATE_REFRESH_TOKEN")
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1 && newVersion > oldVersion) {
            val CREATE_REFRESH_TOKEN =
                "CREATE TABLE IF NOT EXISTS" + " " + TABLE_REFRESH_TOKEN + "(" + KEY_REFRESH_TOKEN + " INTEGER" + ")"
            db.execSQL(CREATE_REFRESH_TOKEN)
            dLog.i("onUpgrade", "onUpgrade: $CREATE_REFRESH_TOKEN")
            return
        }
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDTOCART)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDTOCART_CUST)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLISTZ_CUST)
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_DETAILS)
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_ROLE)
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_COOKIE)
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_TOKEN)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGE_SELECTION)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_TYPE)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REFRESH_TOKEN)
        // Create tables again
        onCreate(db)
    }

    fun insert(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_ADDTOCART, id)
        sqLiteDatabase.insert(TABLE_ADDTOCART, null, initialValues)
        return id
    }

    fun insertlogin(id: Int): Int {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_LOGIN, id)
        sqLiteDatabase.insert(LOGIN_DETAILS, null, initialValues)
        return id
    }

    fun insertRole(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_ROLE, id)
        sqLiteDatabase.insert(LOGIN_ROLE, null, initialValues)
        return id
    }

    fun insertCookie(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_COOKIE, id)
        sqLiteDatabase.insert(LOGIN_COOKIE, null, initialValues)
        return id
    }

    fun insertRefreshToken(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_REFRESH_TOKEN, id)
        sqLiteDatabase.insert(TABLE_REFRESH_TOKEN, null, initialValues)
        return id
    }

    fun insertTerms(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_LOGIN_TERMS, id)
        sqLiteDatabase.insert(TABLE_TERMS, null, initialValues)
        return id
    }

    fun insert_wishlist(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_WISHLIST, id)
        sqLiteDatabase.insert(TABLE_WISHLIST, null, initialValues)
        return id
    }

    fun insert_addtocart_cust(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_ADDTOCART_CUST, id)
        sqLiteDatabase.insert(TABLE_ADDTOCART_CUST, null, initialValues)
        return id
    }

    fun insert_wishlist_cust(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_WISHLIST_CUST, id)
        sqLiteDatabase.insert(TABLE_WISHLISTZ_CUST, null, initialValues)
        return id
    }

    fun insert_token(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_TOKEN, id)
        sqLiteDatabase.insert(DEVICE_TOKEN, null, initialValues)
        return id
    }

    fun insert_lang_selection(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_DATA_LANG, id)
        sqLiteDatabase.insert(TABLE_LANGUAGE_SELECTION, null, initialValues)
        return id
    }

    fun insertLoginType(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_LOGIN_TYPE, id)
        sqLiteDatabase.insert(TABLE_LOGIN_TYPE, null, initialValues)
        return id
    }

    fun remove(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_ADDTOCART, " addtocart = '$id'", null)
        return id
    }

    fun removeRole() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(LOGIN_ROLE, null, null)
    }

    fun removeLoginType() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_LOGIN_TYPE, null, null)
    }

    fun removeToken() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(DEVICE_TOKEN, null, null)
    }

    fun removeCookie() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(LOGIN_COOKIE, null, null)
    }

    fun removeRefreshToken() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_REFRESH_TOKEN, null, null)
    }

    fun removeTerms() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_TERMS, null, null)
    }

    fun removeLogin() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(LOGIN_DETAILS, null, null)
    }

    fun removeAll() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_ADDTOCART, null, null)
    }

    fun removeAllWishList() {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_WISHLIST, null, null)
    }

    fun remove_wishlist(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_WISHLIST, " wishlist = '$id'", null)
        return id
    }

    fun remove_addtocart_cust(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_ADDTOCART_CUST, " addtocartcust = '$id'", null)
        return id
    }

    fun remove_wishlist_cust(id: String): String {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.delete(TABLE_WISHLISTZ_CUST, " wishlistcust = '$id'", null)
        return id
    }

    // Select All Query
    val allData: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + TABLE_ADDTOCART
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val allWishlist: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + TABLE_WISHLIST
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val allLogin: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + LOGIN_DETAILS
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val allRole: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + LOGIN_ROLE
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val loginType: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + TABLE_LOGIN_TYPE
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val cookie: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + LOGIN_COOKIE
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val terms: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + TABLE_TERMS
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val token: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + DEVICE_TOKEN
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val languageSelction: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + TABLE_LANGUAGE_SELECTION
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val allAddtocart_cust: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + TABLE_ADDTOCART_CUST
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val allWishlist_cust: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + TABLE_WISHLISTZ_CUST
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    // Select All Query
    val refreshToken: ArrayList<String>
        get() {
            val dataList = ArrayList<String>()
            // Select All Query
            val selectQuery = "SELECT  * FROM " + TABLE_REFRESH_TOKEN
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    dataList.add(cursor.getString(0))
                } while (cursor.moveToNext())
            }
            return dataList
        }

    companion object {
        // All Static variables
        // Database Version
        private const val DATABASE_VERSION = 2

        // Database Name
        private const val DATABASE_NAME = "dukan"

        // Contacts table name
        private const val TABLE_ADDTOCART = "addtocarttable"
        private const val TABLE_WISHLIST = "wishlisttable"
        private const val TABLE_ADDTOCART_CUST = "addtocarttablecust"
        private const val TABLE_WISHLISTZ_CUST = "wishlisttablecust"
        private const val LOGIN_DETAILS = "logindetails"
        private const val LOGIN_ROLE = "loginrole"
        private const val LOGIN_COOKIE = "logincookie"
        private const val DEVICE_TOKEN = "logintoken"
        private const val TABLE_LANGUAGE_SELECTION = "language"
        private const val TABLE_LOGIN_TYPE = "login_type"
        private const val TABLE_TERMS = "terms"
        private const val TABLE_REFRESH_TOKEN = "refresh_token"

        // Contacts Table Columns names
        private const val KEY_ID = "id"
        private const val KEY_DATA_ADDTOCART = "addtocart"
        private const val KEY_DATA_WISHLIST = "wishlist"
        private const val KEY_DATA_ADDTOCART_CUST = "addtocartcust"
        private const val KEY_DATA_WISHLIST_CUST = "wishlistcust"
        private const val KEY_DATA_LOGIN = "login"
        private const val KEY_DATA_ROLE = "role"
        private const val KEY_DATA_COOKIE = "cookie"
        private const val KEY_DATA_TOKEN = "token"
        private const val KEY_DATA_LANG = "lang"
        private const val KEY_LOGIN_TYPE = "type"
        private const val KEY_LOGIN_TERMS = "key_terms"
        private const val KEY_REFRESH_TOKEN = "key_refresh_token"
    }
}