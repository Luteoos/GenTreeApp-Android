package io.github.luteoos.gent.view.recyclerviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.luteoos.gent.R
import io.github.luteoos.gent.network.api.dataobjects.CommentDto
import kotlinx.android.synthetic.main.rv_comments.view.*

class RVCommentsPerson(val listOfComments: MutableList<CommentDto>,
                       val context: Context
) : RecyclerView.Adapter<RVCommentsPerson.RVCommentsVH>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RVCommentsVH =
        RVCommentsVH(LayoutInflater.from(context).inflate(R.layout.rv_comments, p0,false))

    override fun getItemCount(): Int = listOfComments.size

    override fun onBindViewHolder(view: RVCommentsPerson.RVCommentsVH, position: Int) {
            view.layout.apply {
                tvComment.text = listOfComments[position].body
            }
    }

    class RVCommentsVH(val view: View): RecyclerView.ViewHolder(view){
        val layout = this.view
    }
}