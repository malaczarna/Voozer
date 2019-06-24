package pl.voozer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.driver_item.view.*
import pl.voozer.R
import pl.voozer.service.model.User

class DriversAdapter(val drivers: List<User>, val context: Context, val listener: OnItemClickListener) : RecyclerView.Adapter<DriversAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun onDriverClick(driver: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.driver_item, parent, false))
    }

    override fun getItemCount(): Int {
        return drivers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDriverName.text = drivers[position].name
        holder.tvDriverDestination.text = drivers[position].destination.name
        holder.bind(driver = drivers[position], listener = listener)

    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvDriverName = view.tvDriverName
        val tvDriverDestination = view.tvDriverDestination
        val llDriver = view.llDriver

        fun bind(driver: User, listener: OnItemClickListener) {
            llDriver.setOnClickListener { listener.onDriverClick(driver) }
        }
    }

}