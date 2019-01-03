package io.github.luteoos.gent.view

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import com.bumptech.glide.Glide
import io.github.luteoos.gent.R
import io.github.luteoos.gent.viewmodels.SplashScreenPresenter
import io.realm.Realm
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.view.activity.LogInActivity
import kotlinx.android.synthetic.main.activity_splashscreen.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk27.coroutines.onClick


class SplashScreen : BaseActivityMVVM<SplashScreenPresenter>() {
    override fun onVMMessage(msg: String?) {
    }

    override fun getLayoutID(): Int = R.layout.activity_splashscreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = SplashScreenPresenter()
        button.onClick { startLogInActivity() }
    }

    private fun startLogInActivity(){
        val intent = Intent(this, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}