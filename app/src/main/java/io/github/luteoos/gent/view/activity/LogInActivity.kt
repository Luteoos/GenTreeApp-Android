package io.github.luteoos.gent.view.activity

import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import com.luteoos.kotlin.mvvmbaselib.BaseViewModel
import io.github.luteoos.gent.R
import io.github.luteoos.gent.session.SessionManager

class LogInActivity : BaseActivityMVVM<BaseViewModel>() {
    override fun getLayoutID(): Int = R.layout.activity_log_in
}