package io.github.luteoos.gent.view.fragment

import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVM
import io.github.luteoos.gent.R
import io.github.luteoos.gent.viewmodels.PersonCardViewModel

class PersonCardFragment : BaseFragmentMVVM<PersonCardViewModel>() {
    override fun getLayoutID(): Int = R.layout.fragment_person_card

    override fun onVMMessage(msg: String?) {
    }
}