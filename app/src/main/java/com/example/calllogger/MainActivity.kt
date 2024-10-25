package com.example.calllogger

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.ScatterChart
import android.content.Context
import android.provider.CallLog
import android.widget.TextView
import androidx.core.text.HtmlCompat
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var launchButton : Button
    lateinit var phoneInput : EditText
    lateinit var scatterChart : ScatterChart
    lateinit var debuggingText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        launchButton = findViewById(R.id.launch_script_btn)
        phoneInput = findViewById(R.id.number_input)
        scatterChart = findViewById(R.id.scatterchart)
        debuggingText = findViewById(R.id.debugging)
        launchButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CALL_LOG), 1)
        } else {
            // Permission already granted, proceed with your logic
            if (v != null) {
                getCallLogs(v.context)
            }
        }
    }

    private fun getCallLogs(context: Context) {
        val calllogsBuffer = mutableListOf<String>()

        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null, null, null, null
        )?.use { cursor ->
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)

            while (cursor.moveToNext()) {
                val phoneNumber = cursor.getString(numberIndex)
                val callerName = cursor.getString(nameIndex)
                val callType = when (cursor.getInt(typeIndex)) {
                    CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> "MISSED"
                    else -> "UNKNOWN"
                }

                // if phoneNumber or callerName match phoneInput.text and callType == INCOMING:
                //      add to buffer
                //
                // getCallLogs -> list of Entries
                // make other function to format into graph property

                val callDate = Date(cursor.getLong(dateIndex))

                calllogsBuffer.add(
                """
                Phone Number: $phoneNumber
                Caller ID: $callerName
                Call Type: $callType
                Call Date: $callDate
                """.trimIndent()
                )
            }
            debuggingText.text = calllogsBuffer.toString()
        }
    }
}