package io.github.luteoos.gent.view.recyclerviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.network.api.response.getTreesListAssignedToUser
import kotlinx.android.synthetic.main.rv_tree_list.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class RVTreesList(val list: MutableList<getTreesListAssignedToUser>,
                  val context: Context) : RecyclerView.Adapter<RVTreesList.RVTreesListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RVTreesListViewHolder =
        RVTreesListViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_tree_list, p0,false))

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(view: RVTreesListViewHolder, position: Int) {
        view.name.text = list[position].name
        view.btnOpen.onClick {
            Toasty.info(context, "NOT IMPLEMENTED").show()
        }
        view.btnDelete.onClick {
            Toasty.info(context, "NOT IMPLEMENTED").show()
        }
    }


    class RVTreesListViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val name = view.tvTreeName
        val btnDelete = view.ivDelete
        val btnOpen = view.ivOpenTree
    }
}