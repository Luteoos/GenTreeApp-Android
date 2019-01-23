package io.github.luteoos.gent.view.recyclerviews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.github.luteoos.gent.R
import io.github.luteoos.gent.network.api.response.MediaDtoResponse
import kotlinx.android.synthetic.main.rv_gallery_person.view.*

class RVGalleryPerson(val mediaList: MutableList<MediaDtoResponse>,
                       val context: Context
) : RecyclerView.Adapter<RVGalleryPerson.RVGalleryVH>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RVGalleryVH =
        RVGalleryVH(LayoutInflater.from(context).inflate(R.layout.rv_gallery_person, p0,false))

    override fun getItemCount(): Int = mediaList.size

    override fun onBindViewHolder(view: RVGalleryPerson.RVGalleryVH, position: Int) {
        Glide.with(context)
            .load(mediaList[position].url)
            .into(view.layout.ivGalleryItem)
    }

    class RVGalleryVH(val view: View): RecyclerView.ViewHolder(view){
        val layout = this.view
    }
}