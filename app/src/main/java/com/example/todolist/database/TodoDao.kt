package com.example.todolist.database

import android.content.ContentValues
import android.content.Context
import com.example.todolist.model.Todo

class TodoDao(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insert(todo: Todo): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_TODO_TITLE, todo.title)
            put(DatabaseHelper.COL_TODO_DESCRIPTION, todo.description)
            put(DatabaseHelper.COL_TODO_COMPLETED, if (todo.isCompleted) 1 else 0)
            put(DatabaseHelper.COL_TODO_CREATED_AT, todo.createdAt)
            put(DatabaseHelper.COL_TODO_USER_ID, todo.userId)
        }
        return db.insert(DatabaseHelper.TABLE_TODOS, null, values)
    }

    fun update(todo: Todo): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_TODO_TITLE, todo.title)
            put(DatabaseHelper.COL_TODO_DESCRIPTION, todo.description)
            put(DatabaseHelper.COL_TODO_COMPLETED, if (todo.isCompleted) 1 else 0)
        }
        return db.update(
            DatabaseHelper.TABLE_TODOS, values,
            "${DatabaseHelper.COL_TODO_ID} = ?",
            arrayOf(todo.id.toString())
        )
    }

    fun delete(todoId: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_TODOS,
            "${DatabaseHelper.COL_TODO_ID} = ?",
            arrayOf(todoId.toString())
        )
    }

    fun getAllByUser(userId: Int): List<Todo> {
        val db = dbHelper.readableDatabase
        val todos = mutableListOf<Todo>()
        val cursor = db.query(
            DatabaseHelper.TABLE_TODOS, null,
            "${DatabaseHelper.COL_TODO_USER_ID} = ?",
            arrayOf(userId.toString()),
            null, null,
            "${DatabaseHelper.COL_TODO_CREATED_AT} DESC"
        )
        while (cursor.moveToNext()) {
            todos.add(
                Todo(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_TITLE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_DESCRIPTION)) ?: "",
                    isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_COMPLETED)) == 1,
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_CREATED_AT)),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_USER_ID))
                )
            )
        }
        cursor.close()
        return todos
    }

    fun getById(todoId: Int): Todo? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_TODOS, null,
            "${DatabaseHelper.COL_TODO_ID} = ?",
            arrayOf(todoId.toString()),
            null, null, null
        )
        val todo = if (cursor.moveToFirst()) {
            Todo(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_TITLE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_DESCRIPTION)) ?: "",
                isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_COMPLETED)) == 1,
                createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_CREATED_AT)),
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TODO_USER_ID))
            )
        } else {
            null
        }
        cursor.close()
        return todo
    }
}
