package io.github.luteoos.gent.view.fragment

import android.os.Bundle
import android.view.View
import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVM
import io.github.luteoos.gent.R
import io.github.luteoos.gent.viewmodels.PersonCardViewModel

class PersonCardFragment() : BaseFragmentMVVM<PersonCardViewModel>() {

    private var uuid = ""

    override fun getLayoutID(): Int = R.layout.fragment_person_card

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun setFragmentPersonUUID(uuid: String){
        this.uuid = uuid
        //TODO do all work with loading personal data here
    }

    override fun onVMMessage(msg: String?) {
    }
}