package io.github.luteoos.gent.view.activity

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.eightbitlab.rxbus.Bus
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.data.PersonListFromTree
import io.github.luteoos.gent.utils.CustomBus
import io.github.luteoos.gent.utils.Event
import io.github.luteoos.gent.utils.OnSwipeDetector
import io.github.luteoos.gent.utils.Parameters
import io.github.luteoos.gent.view.fragment.PersonCardFragment
import io.github.luteoos.gent.view.recyclerviews.RVRelatedPersonsList
import io.github.luteoos.gent.viewmodels.TreeViewModel
import kotlinx.android.synthetic.main.activity_tree.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class TreeActivity : BaseActivityMVVM<TreeViewModel>() {
    private lateinit var treeUUID : String
    private lateinit var personCardFragment: PersonCardFragment
    private var dialog: AlertDialog? = null

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
        card_person_fragment.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        viewModel = TreeViewModel()
        connectToVMMessage()
        getDataFromIntent()
        setBindings()
        connectToBus()
        viewModel.getTreeFromRest(treeUUID)
        personCardFragment = PersonCardFragment()
        setFragment(personCardFragment)
        card_person_fragment.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        setFragment(personCardFragment)
        Bus.unregister(this)
        connectToBus()
    }

    override fun onPause() {
        super.onPause()
        Bus.unregister(this)
    }

    override fun finish() {
        PersonListFromTree.Clear()
        super.finish()
    }

    override fun onVMMessage(msg: String?) {
        when(msg){
            viewModel.GET_API_ERROR -> {
                switchSpinnerVisibility(false)
                Toasty.error(this, R.string.api_error).show()
            }
            viewModel.GET_LIST_SUCCESSFUL -> {
                switchSpinnerVisibility(false)
                if (PersonListFromTree.list!!.isNotEmpty() && PersonListFromTree.list != null) {
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
        btnAllList.onClick {
            showRVPersonsListDialogBox()
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
        CustomBus.messageWithUUID.observe(this, Observer {
                        when(it!!.message) {
                            Parameters.SWITCH_TO_PERSON_WITH_UUID -> {
                                if(it.uuid != "") {
                                    card_person_fragment.visibility = View.VISIBLE
                                    this.personCardFragment.setFragmentPersonUUID(it.uuid)
                                    if (dialog != null)
                                        dialog!!.dismiss()
                                }
                            }
                        } })
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

    private fun showRVPersonsListDialogBox(){
        if(PersonListFromTree.list != null) {
            val rv = RecyclerView(this)
            dialog = AlertDialog.Builder(this, R.style.DialogTheme)
                .setCancelable(true)
                .setView(rv.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = RVRelatedPersonsList(PersonListFromTree.getAllPersons(), context)
                })
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
        }
    }
}