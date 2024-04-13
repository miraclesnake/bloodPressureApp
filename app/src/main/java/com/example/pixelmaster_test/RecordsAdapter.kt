package com.example.pixelmaster_test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class RecordsAdapter(private var context: Context, private var recordsList: List<RecordBP>):
    RecyclerView.Adapter<RecordsAdapter.RecordViewHolder>() {
    private val dateFormatter = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.UK)
    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var systolicBP: TextView = itemView.findViewById(R.id.systolicBP)
        var diastolicBP: TextView = itemView.findViewById(R.id.diastolicBP)
        var dateTime: TextView = itemView.findViewById(R.id.textViewDateTime)
        var pulse: TextView = itemView.findViewById(R.id.textViewPulseRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false)
        return RecordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.systolicBP.text = recordsList[position].systolicBP.toString()
        holder.diastolicBP.text = recordsList[position].diastolicBP.toString()
        holder.pulse.text = context.getString(R.string.pulse_record, recordsList[position].pulse)
        holder.dateTime.text = dateFormatter.format(recordsList[position].timestamp)
    }
}