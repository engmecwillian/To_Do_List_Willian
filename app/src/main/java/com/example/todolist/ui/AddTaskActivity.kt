package com.example.todolist.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.sql.Date
import java.util.*
import com.example.todolist.extensions.model.Task as Task

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilTime.text = it.time
            }
        }

        insertListeners()
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text? = Date(date:it+offset).format()

            }
            datePicker.show(supportFragmentManager, tag: "DATE_PICKER_TAG")
        }

        binding.tilTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0$(timePicker.minute)" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0$(timePicker.hour)" else timePicker.hour
                binding.tilTime.text = "$hour:$minute"

            }
            timePicker.show(supportFragmentManager, tag:null)

        }
        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.btnNewTask.setOnClickListener {

            val task = Task(
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                time = binding.tilTime.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}
