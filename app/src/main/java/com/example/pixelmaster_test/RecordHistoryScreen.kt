package com.example.pixelmaster_test

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pixelmaster_test.databinding.FragmentRecordHistoryScreenBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class RecordHistoryScreen : Fragment() {
    private lateinit var recordScreen: FragmentRecordHistoryScreenBinding
    private var recordsList = ArrayList<RecordBP>()
    private lateinit var adapter: RecordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        recordScreen = FragmentRecordHistoryScreenBinding.inflate(inflater, container, false)
        return recordScreen.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recordScreen.historyRecord.layoutManager = LinearLayoutManager(requireActivity())

        if (readFromJson("records.json").contentEquals("1")) {
            recordScreen.textView3.hint = getString(R.string.no_records)
            recordScreen.textView3.visibility = View.VISIBLE
        } else {
            recordScreen.textView3.visibility = View.INVISIBLE
            val recordsFromJson: List<String> = readFromJson("records.json").split("||")
            recordObjectFromFile(recordsFromJson, recordsList)
            adapter = RecordsAdapter(requireActivity(), recordsList)
            recordScreen.historyRecord.adapter = adapter
            recordScreen.historyRecord.visibility = View.VISIBLE
        }
    }

    fun readFromJson(fileName: String): String {
        val file = File(requireActivity().filesDir, fileName)
        return if (file.length() != 0L) {
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("||")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            stringBuilder.toString()
        } else {
            "1"
        }
    }


    fun recordObjectFromFile(jsonObjectString: List<String>, finalList: ArrayList<RecordBP>) {
        jsonObjectString.forEach {
            if (it.isNotBlank()) {
                try {
                    val jsonObject = JSONObject(it)

                    finalList.add(
                        RecordBP(
                            jsonObject.get("systolicBP") as Int,
                            jsonObject.get("diastolicBP") as Int,
                            jsonObject.get("pulse") as Int,
                            jsonObject.get("timestamp") as Long
                        )
                    )
                } catch (e: JSONException) {
                    Log.e("retrieving records from JSON", e.message!!)
                }
            }
        }
    }
}