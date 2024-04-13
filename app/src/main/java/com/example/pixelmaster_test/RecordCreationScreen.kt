package com.example.pixelmaster_test

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.pixelmaster_test.databinding.FragmentRecordCreationScreenBinding
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Locale

class RecordCreationScreen : Fragment() {
    private lateinit var recordCreationScreen: FragmentRecordCreationScreenBinding
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.UK)
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.UK)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        recordCreationScreen =
            FragmentRecordCreationScreenBinding.inflate(inflater, container, false)

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
        toolbar.setBackgroundColor(ContextCompat.getColor(requireActivity(), com.google.android.material.R.color.design_default_color_primary))
        return recordCreationScreen.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numberPickerDS = recordCreationScreen.numberPickerDiastolic
        val numberPickerSS = recordCreationScreen.numberPickerSystolic
        val numberPickerP = recordCreationScreen.numberPickerPulse

        numberPickerDS.minValue = 0
        numberPickerDS.maxValue = 200
        numberPickerSS.minValue = 0
        numberPickerSS.maxValue = 200
        numberPickerP.minValue = 0
        numberPickerP.maxValue = 200

        recordCreationScreen.buttonDatePick.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    recordCreationScreen.buttonDatePick.text =
                        dateFormatter.format(calendar.timeInMillis)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        recordCreationScreen.buttonTimePick.setOnClickListener {
            TimePickerDialog(
                requireActivity(),
                { _, hourOfDay, minute ->
                    calendar.apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                    }
                    recordCreationScreen.buttonTimePick.text =
                        timeFormatter.format(calendar.timeInMillis)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        recordCreationScreen.buttonSaveRecord.setOnClickListener {
            addToJson(
                addRecord(
                    RecordBP(
                        numberPickerSS.value,
                        numberPickerDS.value,
                        numberPickerP.value,
                        calendar.timeInMillis
                    )
                ).toString(), "records.json"
            )
            Toast.makeText(requireActivity(), "The record was saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addRecord(record: RecordBP): JSONObject {
        return JSONObject()
            .put("systolicBP", record.systolicBP)
            .put("diastolicBP", record.diastolicBP)
            .put("pulse", record.pulse)
            .put("timestamp", record.timestamp)
    }

    private fun addToJson(jsonString: String, fileName: String) {
        val fileWriter = FileWriter(File(requireActivity().filesDir, fileName), true)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonString)
        bufferedWriter.newLine()
        bufferedWriter.close()
    }
}