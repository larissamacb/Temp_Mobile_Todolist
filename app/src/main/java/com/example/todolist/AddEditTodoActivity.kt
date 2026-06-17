package com.example.todolist

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.database.TodoDao
import com.example.todolist.model.Todo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddEditTodoActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var cbCompleted: CheckBox
    private lateinit var btnSave: Button
    private lateinit var tvScreenTitle: TextView
    private lateinit var todoDao: TodoDao
    private var userId: Int = 0
    private var todoId: Int = -1
    private var existingTodo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_todo)

        userId = intent.getIntExtra("USER_ID", 0)
        todoId = intent.getIntExtra("TODO_ID", -1)
        todoDao = TodoDao(this)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        cbCompleted = findViewById(R.id.cbCompleted)
        btnSave = findViewById(R.id.btnSave)
        tvScreenTitle = findViewById(R.id.tvScreenTitle)

        if (todoId != -1) {
            tvScreenTitle.text = "Editar Tarefa"
            existingTodo = todoDao.getById(todoId)
            existingTodo?.let {
                etTitle.setText(it.title)
                etDescription.setText(it.description)
                cbCompleted.isChecked = it.isCompleted
            }
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val isCompleted = cbCompleted.isChecked

            if (title.isEmpty()) {
                Toast.makeText(this, "O título é obrigatório", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (todoId == -1) {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                todoDao.insert(Todo(
                    title = title,
                    description = description,
                    isCompleted = isCompleted,
                    createdAt = dateFormat.format(Date()),
                    userId = userId
                ))
                Toast.makeText(this, "Tarefa criada!", Toast.LENGTH_SHORT).show()
            } else {
                existingTodo?.let {
                    todoDao.update(it.copy(title = title, description = description, isCompleted = isCompleted))
                    Toast.makeText(this, "Tarefa atualizada!", Toast.LENGTH_SHORT).show()
                }
            }
            finish()
        }

        findViewById<TextView>(R.id.tvCancel).setOnClickListener { finish() }
    }
}
