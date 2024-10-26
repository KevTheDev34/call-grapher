package com.example.calllogger

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.data.Entry
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var launchButton : Button
    lateinit var phoneInput : EditText
    lateinit var scatterChart : ScatterChart
    lateinit var debuggingText : TextView
    lateinit var callEntries : List<Entry>

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
                callEntries = getCallLogs(v.context)
//                scatterChart.scatterData(callEntries)
            }
        }
    }

    private fun getCallLogs(context: Context): List<Entry> {
        val calllogsBuffer = mutableListOf<Entry>()

        context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null, null, null, null
        )?.use { cursor ->
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)

            while (cursor.moveToNext()) {
                val callDate = Date(cursor.getLong(dateIndex))
                var phoneNumber = cursor.getString(numberIndex)
                val callerName = cursor.getString(nameIndex)
                val callType = when (cursor.getInt(typeIndex)) {
                    CallLog.Calls.OUTGOING_TYPE -> "OUTGOING"
                    CallLog.Calls.INCOMING_TYPE -> "INCOMING"
                    CallLog.Calls.MISSED_TYPE -> "MISSED"
                    else -> "UNKNOWN"
                }

                val re = Regex("[^A-Za-z0-9 ]")
                phoneNumber = re.replace(phoneNumber, "")

                // if phoneNumber or callerName match phoneInput.text and callType == INCOMING:
                if ((phoneNumber.equals(phoneInput.text.toString(), true) ||
                            callerName.equals(phoneInput.text.toString(),true)) &&
                    callType.equals("INCOMING", true)) {

                    // convert into Entry and add to list
                    // hour of day = callDate[11:16] (07:16) -> 7.266 (fraction of hr)              (x-value)
                    // day of week = callDate[:3] (Mon, Tue, Wed...) -> 1.0, 2.0, 3.0...            (y-value)
                    // Note: i am very stupid, the Date class has methods

                    val x_value : Float = getDayOfWeekAsFloat(callDate.toString())
                    val y_value : Float = getHourOfDayAsFloat(callDate)
                    val currEntry = Entry(x_value, y_value)

                    calllogsBuffer.add(currEntry)
//                    calllogsBuffer.add(
//                        """
//                        Phone Number: $phoneNumber
//                        Caller ID: $callerName
//                        Call Type: $callType
//                        Call Date: $callDate
//                        """.trimIndent()
//                    )
                }
                // getCallLogs -> list of Entries
                // make other function to format into graph properly
            }
            debuggingText.text = calllogsBuffer.toString()
        }
        return calllogsBuffer
    }

    private fun getDayOfWeekAsFloat(callDate: String): Float {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Adjust the pattern if needed
        val date = LocalDate.parse(callDate, formatter)
        val dayOfWeek = date.dayOfWeek
        return dayOfWeek.value.toFloat()
    }

    private fun getHourOfDayAsFloat(callDate: Date): Float {
        val dateFormat = SimpleDateFormat("HH", Locale.getDefault())
        val hourString = dateFormat.format(callDate)
        return hourString.toFloat()
    }
}