package io.github.luteoos.gent.view.recyclerviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eightbitlab.rxbus.Bus
import io.github.luteoos.gent.R
import io.github.luteoos.gent.data.PersonListFromTree
import io.github.luteoos.gent.network.api.dataobjects.PersonDTO
import io.github.luteoos.gent.network.api.response.getTreesListAssignedToUser
import io.github.luteoos.gent.utils.Event
import io.github.luteoos.gent.utils.Parameters
import kotlinx.android.synthetic.main.rv_related_persons_list.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class RVRelatedPersonsList(val listPersonsUUID: MutableList<String>,
                           val context: Context) : RecyclerView.Adapter<RVRelatedPersonsList.RVRelatedPersonsListVH>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RVRelatedPersonsListVH =
        RVRelatedPersonsListVH(LayoutInflater.from(context).inflate(R.layout.rv_related_persons_list, p0,false))

    override fun getItemCount(): Int = listPersonsUUID.size

    override fun onBindViewHolder(view: RVRelatedPersonsList.RVRelatedPersonsListVH, position: Int) {
        val personData = PersonListFromTree.getPerson(listPersonsUUID[position])
        if(personData != null) {
            view.layout.apply {
                tvName.text = personData.details.name
                tvSurname.text = personData.details.surname
            }
        }else
            view.layout.tvName.text = context.getString(R.string.api_error)
        view.layout.onClick {
            Bus.send(Event.MessageWithUUID(UUID.fromString(listPersonsUUID[position]),
                Parameters.SWITCH_TO_PERSON_WITH_UUID))
        }
    }

    class RVRelatedPersonsListVH(val view: View): RecyclerView.ViewHolder(view){
        val layout = this.view
    }
}