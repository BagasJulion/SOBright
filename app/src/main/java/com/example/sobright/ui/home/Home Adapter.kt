package com.example.sobright.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sobright.databinding.CardItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.CardViewHolder>() {

    class CardViewHolder (internal val binding: CardItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.CardViewHolder {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        when (position) {
            0 -> {
                holder.binding.tvYear.text = "Tahun"
                holder.binding.tvNumber.text = "$year"
            }
            1 ->{
                holder.binding.tvYear.text = "Bulan"
                holder.binding.tvNumber.text = "$month"
            }
            2 ->{
                holder.binding.tvYear.text = "Hari"
                holder.binding.tvNumber.text = "$day"
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}