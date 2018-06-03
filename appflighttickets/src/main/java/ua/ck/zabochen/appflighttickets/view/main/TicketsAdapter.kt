package ua.ck.zabochen.appflighttickets.view.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.ck.zabochen.appflighttickets.R
import ua.ck.zabochen.appflighttickets.network.model.Ticket

class TicketsAdapter(private val ticketList: List<Ticket>) : RecyclerView.Adapter<TicketsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ticket, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ticketList[position])
    }

    override fun getItemCount(): Int {
        return if (ticketList.isNotEmpty()) ticketList.size else 0
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bind(ticket: Ticket) {

        }

    }
}