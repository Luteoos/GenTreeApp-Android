package io.github.luteoos.gent.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.eightbitlab.rxbus.Bus
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.data.PersonListFromTree
import io.github.luteoos.gent.utils.Event
import io.github.luteoos.gent.utils.OnSwipeDetector
import io.github.luteoos.gent.utils.Parameters
import io.github.luteoos.gent.view.fragment.PersonCardFragment
import io.github.luteoos.gent.viewmodels.TreeViewModel
import kotlinx.android.synthetic.main.activity_tree.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class TreeActivity : BaseActivityMVVM<TreeViewModel>() {
    private lateinit var treeUUID : String
    private lateinit var personCardFragment: PersonCardFragment

    override fun getLayoutID(): Int = R.layout.activity_tree

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = TreeViewModel()
        connectToVMMessage()
        getDataFromIntent()
        setBindings()
        connectToBus()
        viewModel.getTreeFromRest(treeUUID)
        personCardFragment = PersonCardFragment()
        setFragment(personCardFragment)
    }

    override fun onResume() {
        super.onResume()
        connectToBus()
    }

    override fun finish() {
        super.finish()
        PersonListFromTree.Clear()
    }

    override fun onVMMessage(msg: String?) {
        when(msg){
            viewModel.GET_API_ERROR -> {
                switchSpinnerVisibility(false)
                Toasty.error(this, R.string.api_error)
            }
            viewModel.GET_LIST_SUCCESSFUL -> {
                switchSpinnerVisibility(false)
                if (PersonListFromTree.list!!.isNotEmpty()) {
                    card_person_fragment.visibility = View.VISIBLE
                    personCardFragment.setFragmentPersonUUID(PersonListFromTree.list!!.first().id)
                }else
                    Toasty.warning(this, R.string.warning_tree_empty).show()
            }
        }
    }

    private fun setBindings(){
        ivBack.onClick {
            onBackPressed()
        }
        csLayoutTreeActivity.setOnTouchListener(object : OnSwipeDetector(this){
            override fun onSwipeBottom() {
                switchSpinnerVisibility(true)
                card_person_fragment.visibility = View.GONE
                personCardFragment.resetUUID()
                viewModel.getTreeFromRest(treeUUID)
            }
        })
    }

    private fun connectToBus(){
        Bus.observe<Event.MessageWithUUID>()
            .subscribe{
                runOnUiThread {
                    when(it.message) {
                        Parameters.SWITCH_TO_PERSON_WITH_UUID -> {
                            card_person_fragment.visibility = View.VISIBLE
                            personCardFragment.setFragmentPersonUUID(it.uuid.toString())
                        }
                }
            }
        }
    }

    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.card_person_fragment, fragment)
            .commitAllowingStateLoss()
    }

    private fun getDataFromIntent(){
        treeUUID = intent.getStringExtra(Parameters.TREE_UUID)
        tvTreeName.text = intent.getStringExtra(Parameters.TREE_NAME)
    }

    private fun switchSpinnerVisibility(boolean: Boolean){
        when(boolean){
            true -> spinner_tree.visibility = View.VISIBLE
            false -> spinner_tree.visibility = View.GONE
        }
    }
}