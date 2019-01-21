package io.github.luteoos.gent.view.recyclerviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.luteoos.gent.R
import io.github.luteoos.gent.network.api.dataobjects.PersonDTO
import io.github.luteoos.gent.network.api.response.getTreesListAssignedToUser

class RVRelatedPersonsList(val listPersonsUUID: MutableList<String>,
                           val context: Context) : RecyclerView.Adapter<RVRelatedPersonsList.RVRelatedPersonsListVH>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RVRelatedPersonsListVH =
        RVRelatedPersonsListVH(LayoutInflater.from(context).inflate(R.layout./*TODO layout add here name +uuid + realtion */, p0,false))

    override fun getItemCount(): Int = listPersonsUUID.size

    override fun onBindViewHolder(view: RVRelatedPersonsList.RVRelatedPersonsListVH, position: Int) {
        view.layout
    }

    class RVRelatedPersonsListVH(val view: View): RecyclerView.ViewHolder(view){
        val layout = this.view
    }
}