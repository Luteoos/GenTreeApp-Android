package io.github.luteoos.gent.view.activity

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.EventLog
import android.view.View
import com.eightbitlab.rxbus.Bus
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.utils.Parameters
import io.github.luteoos.gent.viewmodels.LogInViewModel
import kotlinx.android.synthetic.main.activity_log_in.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk27.coroutines.onClick
import android.support.v4.os.HandlerCompat.postDelayed
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide


class LogInActivity : BaseActivityMVVM<LogInViewModel>() {

    override fun getLayoutID(): Int = R.layout.activity_log_in

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LogInViewModel()
        this.connectToVMMessage()
        setBinding()
        startAnimationSpin()
    }

    override fun onVMMessage(msg: String?){
        when(msg){
            Parameters.LOG_IN_SUCCESS -> onLogInSuccess()
            Parameters.LOG_IN_FAILED -> onLogInFailed()
        }
    }

    fun hideProgressBar(){
        progressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun onLogInSuccess(){
        hideProgressBar()
        check.check()
        btnLogIn.isClickable = false
        Handler().postDelayed(({ startMainActivity() }), 700)
    }

    private fun onLogInFailed(){
        hideProgressBar()
        Toasty.error(this, getString(R.string.login_failed)).show()
    }

    private fun setBinding(){
        btnExit.onClick {
            onBackPressed()
        }
        cl_background.onClick {
            hideKeyboard()
        }
        btnLogIn.onClick {
            if(!isNetworkOnLine)
                Toasty.error(this@LogInActivity, R.string.api_error).show()
            else{
                showProgressBar()
                logInValdiateText()
            }
        }
    }

    private fun logInValdiateText(){
        if(etUsername.text.isNullOrEmpty() || etPassword.text.isNullOrEmpty()){
            hideProgressBar()
            Toasty.error(this, getString(R.string.login_failed)).show()
        }
        else{
            viewModel.LogInRest(etUsername.text.toString(), etPassword.text.toString())
        }
    }

    private fun startAnimationSpin(){
        Glide.with(this)
            .load(R.drawable.gentree_circle)
            .into(ivLogoSpin)
        val spin = AnimationUtils.loadAnimation(this, R.anim.spin_infinite)
        ivLogoSpin.startAnimation(spin)
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainScreenActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}