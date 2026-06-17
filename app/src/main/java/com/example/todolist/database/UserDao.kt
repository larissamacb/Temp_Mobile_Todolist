package com.example.todolist.database

import android.content.ContentValues
import android.content.Context
import com.example.todolist.model.User
import java.security.MessageDigest

class UserDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun register(user: User): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_USER_NAME, user.username)
            put(DatabaseHelper.COL_USER_EMAIL, user.email)
            put(DatabaseHelper.COL_USER_PASSWORD, hashPassword(user.password))
        }
        return db.insert(DatabaseHelper.TABLE_USERS, null, values)
    }

    fun login(username: String, password: String): User? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            null,
            "${DatabaseHelper.COL_USER_NAME} = ? AND ${DatabaseHelper.COL_USER_PASSWORD} = ?",
            arrayOf(username, hashPassword(password)),
            null, null, null
        )
        val user = if (cursor.moveToFirst()) {
            User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_PASSWORD))
            )
        } else {
            null
        }
        cursor.close()
        return user
    }

    fun usernameExists(username: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COL_USER_ID),
            "${DatabaseHelper.COL_USER_NAME} = ?",
            arrayOf(username),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun emailExists(email: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_USERS,
            arrayOf(DatabaseHelper.COL_USER_ID),
            "${DatabaseHelper.COL_USER_EMAIL} = ?",
            arrayOf(email),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
