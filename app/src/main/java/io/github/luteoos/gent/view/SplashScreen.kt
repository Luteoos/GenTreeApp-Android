package io.github.luteoos.gent.view

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import io.github.luteoos.gent.R
import io.github.luteoos.gent.viewmodels.SplashScreenViewModel
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.view.activity.LogInActivity
import io.github.luteoos.gent.view.activity.MainScreenActivity
import kotlinx.android.synthetic.main.activity_splashscreen.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class SplashScreen : BaseActivityMVVM<SplashScreenViewModel>() {
    override fun onVMMessage(msg: String?) {
    }

    override fun getLayoutID(): Int = R.layout.activity_splashscreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = SplashScreenViewModel()
        Glide.with(this)
            .load(getDrawable(R.drawable.gentree_logo))
            .into(ivLogo)
        ivLogo.onClick { startLogInActivity() }
        if(isAlreadyLoggedIn())
            startMainScreenActivity()
    }

    private fun startLogInActivity(){
        val intent = Intent(this, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun isAlreadyLoggedIn(): Boolean = SessionManager.accessToken != null && SessionManager.userUUDString != null
                                                && SessionManager.accessToken != "" && SessionManager.userUUDString != ""

    private fun startMainScreenActivity(){
        val intent = Intent(this, MainScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}