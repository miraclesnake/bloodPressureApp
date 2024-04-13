package com.example.pixelmaster_test

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pixelmaster_test.databinding.FragmentMainScreenBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class MainScreen : Fragment() {
    private lateinit var mainScreen: FragmentMainScreenBinding
    private var recordsBP = ArrayList<RecordBP>()
    private lateinit var adapter: RecordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainScreen = FragmentMainScreenBinding.inflate(inflater, container, false)
        return mainScreen.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainScreen.buttonToAllHistory.setOnClickListener {
            val direction = MainScreenDirections.actionMainScreenToRecordHistoryScreen()
            if (requireActivity() is MainActivity) {
                this.findNavController().navigate(direction)
            }
        }

        mainScreen.buttonAddRecord.setOnClickListener {
            val direction = MainScreenDirections.actionMainScreenToRecordCreationScreen()
            if (requireActivity() is MainActivity) {
                this.findNavController().navigate(direction)
            }
        }

        if (readFromJson("records.json").contentEquals("1")) {
            mainScreen.textView.hint = getString(R.string.no_records)
            mainScreen.textView.visibility = View.VISIBLE
        } else {
            mainScreen.textView.visibility = View.INVISIBLE
            recordsBP.clear()
            val recordsFromJson: List<String> = readFromJson("records.json").split("||")
            recordObjectFromFile(recordsFromJson, recordsBP)
            mainScreen.recyclerViewRecentRecords.layoutManager =
                LinearLayoutManager(requireActivity())
            adapter = RecordsAdapter(requireActivity(), recordsBP)
            mainScreen.recyclerViewRecentRecords.adapter = adapter
            mainScreen.recyclerViewRecentRecords.visibility = View.VISIBLE
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
        jsonObjectString.reversed().take(4).forEach {
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