package com.example.todolist.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "TodoApp.db"
        const val DATABASE_VERSION = 1

        const val TABLE_USERS = "users"
        const val COL_USER_ID = "id"
        const val COL_USER_NAME = "username"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_PASSWORD = "password"

        const val TABLE_TODOS = "todos"
        const val COL_TODO_ID = "id"
        const val COL_TODO_TITLE = "title"
        const val COL_TODO_DESCRIPTION = "description"
        const val COL_TODO_COMPLETED = "is_completed"
        const val COL_TODO_CREATED_AT = "created_at"
        const val COL_TODO_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_NAME TEXT NOT NULL UNIQUE,
                $COL_USER_EMAIL TEXT NOT NULL UNIQUE,
                $COL_USER_PASSWORD TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_TODOS (
                $COL_TODO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TODO_TITLE TEXT NOT NULL,
                $COL_TODO_DESCRIPTION TEXT,
                $COL_TODO_COMPLETED INTEGER DEFAULT 0,
                $COL_TODO_CREATED_AT TEXT NOT NULL,
                $COL_TODO_USER_ID INTEGER NOT NULL,
                FOREIGN KEY ($COL_TODO_USER_ID) REFERENCES $TABLE_USERS($COL_USER_ID)
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
}
