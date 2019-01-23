package io.github.luteoos.gent.view.recyclerviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.luteoos.gent.R
import io.github.luteoos.gent.network.api.dataobjects.EventDto
import kotlinx.android.synthetic.main.rv_event_list.view.*

class RVEventsPerson(val eventList: MutableList<EventDto>,
                     val context: Context
) : RecyclerView.Adapter<RVEventsPerson.RVEventVH>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RVEventVH =
        RVEventVH(LayoutInflater.from(context).inflate(R.layout.rv_event_list, p0,false))

    override fun getItemCount(): Int = eventList.size

    override fun onBindViewHolder(view: RVEventsPerson.RVEventVH, position: Int) {
        view.layout.apply {
            tvDate.text = eventList[position].getFormattedDate()
            tvType.text = eventList[position].type.toUpperCase()
            tvDescription.text = if(eventList[position].description != "")
                eventList[position].description
            else
                context.getString(R.string.empty_description)
        }
    }

    class RVEventVH(val view: View): RecyclerView.ViewHolder(view){
        val layout = this.view
    }
}