package io.github.luteoos.gent.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.view.View
import com.bumptech.glide.Glide
import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.viewmodels.MyProfileFragmentViewModel
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx
import java.util.*

class MyProfileFragment : BaseFragmentMVVM<MyProfileFragmentViewModel>() {

    override fun getLayoutID(): Int = R.layout.fragment_my_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        viewModel = MyProfileFragmentViewModel()
        this.connectToVMMessage()
        setBindings()
        initialize()
    }

    override fun onVMMessage(msg: String?) {
        when(msg){
            viewModel.AVATAR_LOAD_SUCCESS -> {
                loadAvatar(viewModel.avatarByteString!!)
                avatarProgressBar.visibility = View.GONE
            }
            viewModel.AVATAR_LOAD_FAILED -> {
                tvNoAvatar.visibility = View.VISIBLE
                avatarProgressBar.visibility = View.GONE
            }
        }
    }

    private fun initialize(){
        tvUsername?.text = SessionManager.username
        if(isNetworkOnLine)
            viewModel.getUserAvatarByte()
        else {
            tvNoAvatar.visibility = View.VISIBLE
            avatarProgressBar.visibility = View.GONE
        }
    }

    private fun  setBindings(){
        btnChangeAvatar.onClick {
            Toasty.info(context!!,"not implemented !").show()
        }
        btnLogout.onClick {
            SessionManager.logout(context!!)
        }
    }

    private fun loadAvatar(bytes: String){
        val byteArray = Base64.decode(bytes, Base64.DEFAULT)
        Glide.with(this)
            .asBitmap()
            .load(byteArray)
            .into(iv_profile_pic)
    }
}