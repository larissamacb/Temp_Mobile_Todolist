package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.adapter.TodoAdapter
import com.example.todolist.database.TodoDao
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvEmpty: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var todoDao: TodoDao
    private lateinit var adapter: TodoAdapter
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        userId = intent.getIntExtra("USER_ID", 0)
        val username = intent.getStringExtra("USERNAME") ?: ""

        todoDao = TodoDao(this)

        recyclerView = findViewById(R.id.recyclerView)
        fabAdd = findViewById(R.id.fabAdd)
        tvEmpty = findViewById(R.id.tvEmpty)
        tvWelcome = findViewById(R.id.tvWelcome)

        tvWelcome.text = "Olá, $username!"

        adapter = TodoAdapter(
            onEdit = { todo ->
                startActivity(Intent(this, AddEditTodoActivity::class.java).apply {
                    putExtra("USER_ID", userId)
                    putExtra("TODO_ID", todo.id)
                })
            },
            onDelete = { todo ->
                AlertDialog.Builder(this)
                    .setTitle("Excluir tarefa")
                    .setMessage("Deseja excluir \"${todo.title}\"?")
                    .setPositiveButton("Excluir") { _, _ ->
                        todoDao.delete(todo.id)
                        loadTodos()
                        Toast.makeText(this, "Tarefa excluída", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            },
            onToggleComplete = { todo ->
                todoDao.update(todo.copy(isCompleted = !todo.isCompleted))
                loadTodos()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditTodoActivity::class.java).apply {
                putExtra("USER_ID", userId)
            })
        }

        findViewById<TextView>(R.id.tvLogout).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTodos()
    }

    private fun loadTodos() {
        val todos = todoDao.getAllByUser(userId)
        adapter.submitList(todos)
        tvEmpty.visibility = if (todos.isEmpty()) View.VISIBLE else View.GONE
    }
}
