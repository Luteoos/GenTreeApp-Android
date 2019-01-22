package io.github.luteoos.gent.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.data.PersonListFromTree
import io.github.luteoos.gent.utils.OnSwipeDetector
import io.github.luteoos.gent.utils.Parameters
import io.github.luteoos.gent.view.recyclerviews.RVRelatedPersonsList
import io.github.luteoos.gent.viewmodels.PersonCardViewModel
import kotlinx.android.synthetic.main.fragment_person_card.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class PersonCardFragment : BaseFragmentMVVM<PersonCardViewModel>() {

    private var uuid = ""

    override fun getLayoutID(): Int = R.layout.fragment_person_card

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = PersonCardViewModel()
    }

    fun setFragmentPersonUUID(uuid: String){
        setAllTvRvToDeafult()
        this.uuid = uuid
        setRV()
        setBindings()
        //TODO do all work with loading personal data here
    }

    private fun setRV(){
        rvChildren.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RVRelatedPersonsList(PersonListFromTree.getPersonRelativesOfTypeUUIDList(uuid,
                PersonListFromTree.RELATION_PARENT),context)
        }
        rvParents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RVRelatedPersonsList(PersonListFromTree.getPersonRelativesOfTypeUUIDList(uuid,
                PersonListFromTree.RELATION_CHILD),context)
        }
        rvSiblings.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RVRelatedPersonsList(PersonListFromTree.getPersonRelativesOfTypeUUIDList(uuid,
                PersonListFromTree.RELATION_SIBLING),context)
        }
        rvMarriage.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RVRelatedPersonsList(PersonListFromTree.getPersonRelativesOfTypeUUIDList(uuid,
                PersonListFromTree.RELATION_MARRIAGE),context)
        }
    }

    private fun setBindings(){
        this.view!!.onClick { setAllTvRvToDeafult() }
        this.view!!.setOnTouchListener(object : OnSwipeDetector(context!!){
            override fun onSwipeTop() {
                setAllTvRvToDeafult()
                rvParents.visibility = View.VISIBLE
            }

            override fun onSwipeLeft() {
                setAllTvRvToDeafult()
                setTvMarriageToVertical(false)
                rvMarriage.visibility = View.VISIBLE
            }

            override fun onSwipeRight() {
                setAllTvRvToDeafult()
                setTvSiblingToVertical(false)
                rvSiblings.visibility = View.VISIBLE
            }

            override fun onSwipeBottom() {
                setAllTvRvToDeafult()
                rvChildren.visibility = View.VISIBLE
            }

            override fun onAnyActionPerformed() {
                setAllTvRvToDeafult()
            }
        })
    }

    private fun setTvMarriageToVertical(boolean: Boolean){
        when(boolean){
            false -> tvMarriageText.text = getString(R.string.card_marriage)
            true -> tvMarriageText.text = getString(R.string.card_marriage_vertical)
        }
    }

    private fun setTvSiblingToVertical(boolean: Boolean){
        when(boolean){
            false -> tvSiblingsText.text = getString(R.string.card_siblings)
            true -> tvSiblingsText.text = getString(R.string.card_siblings_vertical)
        }
    }

    private fun setAllTvRvToDeafult(){
        setTvSiblingToVertical(true)
        setTvMarriageToVertical(true)
        rvChildren.visibility = View.GONE
        rvParents.visibility = View.GONE
        rvMarriage.visibility = View.GONE
        rvSiblings.visibility = View.GONE
    }

    override fun onVMMessage(msg: String?) {
    }
}