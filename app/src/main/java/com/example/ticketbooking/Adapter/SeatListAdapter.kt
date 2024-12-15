package com.example.ticketbooking.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.Models.Seat
import com.example.ticketbookingapp.R
import com.example.ticketbookingapp.databinding.SeatItemBinding

class SeatListAdapter(
    private val seatList: List<Seat>,
    private val context: Context,
    private val selectedSeat: SelectedSeat
) : RecyclerView.Adapter<SeatListAdapter.SeatViewholder>() {

    private val selectedSeatName = ArrayList<String>()  // Corrected variable name

    class SeatViewholder(val binding: SeatItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewholder {
        return SeatViewholder(
            SeatItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SeatViewholder, position: Int) {
        val seat = seatList[position]
        holder.binding.seat.text = seat.name
        when (seat.status) {  // Use the correct property name 'status'
            Seat.SeatStatus.AVAILABLE -> {
                holder.binding.seat.setBackgroundResource(R.drawable.ic_seat_available)
                holder.binding.seat.setTextColor(context.getColor(R.color.white))
            }
            Seat.SeatStatus.SELECTED -> {
                holder.binding.seat.setBackgroundResource(R.drawable.ic_seat_selected)
                holder.binding.seat.setTextColor(context.getColor(R.color.black))
            }
            Seat.SeatStatus.UNAVAILABLE -> {
                holder.binding.seat.setBackgroundResource(R.drawable.ic_seat_unavailable)
                holder.binding.seat.setTextColor(context.getColor(R.color.grey))
            }
        }

        holder.binding.seat.setOnClickListener {
            when (seat.status) {
                Seat.SeatStatus.AVAILABLE -> {
                    seat.status = Seat.SeatStatus.SELECTED  // Corrected 'Status' to 'status'
                    selectedSeatName.add(seat.name)  // Corrected variable name
                    notifyItemChanged(position)
                }
                Seat.SeatStatus.SELECTED -> {
                    seat.status = Seat.SeatStatus.AVAILABLE  // Corrected 'Status' to 'status'
                    selectedSeatName.add(seat.name)  // Corrected variable name
                    notifyItemChanged(position)
                }
                else -> {}
            }
            val selected = selectedSeatName.joinToString(",")
            selectedSeat.onSeatSelected(selected, selectedSeatName.size)  // Corrected method call
        }
    }

    override fun getItemCount(): Int = seatList.size

    interface SelectedSeat {
        fun onSeatSelected(selectedName: String, num: Int)
    }
}
