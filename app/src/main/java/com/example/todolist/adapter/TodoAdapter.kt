package com.example.todolist.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.model.Todo

class TodoAdapter(
    private val onEdit: (Todo) -> Unit,
    private val onDelete: (Todo) -> Unit,
    private val onToggleComplete: (Todo) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var todos: List<Todo> = emptyList()

    fun submitList(newList: List<Todo>) {
        todos = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount() = todos.size

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
        private val cbCompleted: CheckBox = itemView.findViewById(R.id.cbCompleted)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        fun bind(todo: Todo) {
            tvTitle.text = todo.title
            tvDescription.text = todo.description
            tvDescription.visibility = if (todo.description.isEmpty()) View.GONE else View.VISIBLE
            tvCreatedAt.text = todo.createdAt.take(16)
            cbCompleted.isChecked = todo.isCompleted

            if (todo.isCompleted) {
                tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            cbCompleted.setOnClickListener { onToggleComplete(todo) }
            btnEdit.setOnClickListener { onEdit(todo) }
            btnDelete.setOnClickListener { onDelete(todo) }
        }
    }
}
