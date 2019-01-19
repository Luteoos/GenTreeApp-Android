package io.github.luteoos.gent.view.fragment

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVMWithoutVM
import io.github.luteoos.gent.R
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeProfileFragment : BaseFragmentMVVMWithoutVM() {
    override fun getLayoutID(): Int = R.layout.fragment_welcome

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //rework so looks better
        Glide.with(this)
            .load(R.drawable.gentree_black)
            .into(ivPic)
    }
}