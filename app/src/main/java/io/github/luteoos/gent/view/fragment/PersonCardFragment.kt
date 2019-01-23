package io.github.luteoos.gent.view.fragment

import android.Manifest
import android.app.Activity
import android.app.Person
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.data.PersonListFromTree
import io.github.luteoos.gent.utils.OnSwipeDetector
import io.github.luteoos.gent.utils.Parameters
import io.github.luteoos.gent.utils.UriResolver
import io.github.luteoos.gent.view.activity.PersonGalleryActivity
import io.github.luteoos.gent.view.recyclerviews.RVCommentsPerson
import io.github.luteoos.gent.view.recyclerviews.RVRelatedPersonsList
import io.github.luteoos.gent.viewmodels.PersonCardViewModel
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.fragment_person_card.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx

class PersonCardFragment : BaseFragmentMVVM<PersonCardViewModel>() {

    private val CHANGE_PERSON_AVATAR = 911
    private val REQUEST_PERMISSION_STORAGE = 910
    private var uuid = ""

    override fun getLayoutID(): Int = R.layout.fragment_person_card

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = PersonCardViewModel()
        this.connectToVMMessage()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CHANGE_PERSON_AVATAR -> {
                    setAvatarSpinnerVisibility(true)
                    val uri = data?.data
                    val file = UriResolver.getFileFromUri(uri!!, context!!.contentResolver)
                    viewModel.uploadMediaToServer(file,uuid)
                }
            }
        }
    }

    fun resetUUID(){
        uuid = ""
    }

    fun setFragmentPersonUUID(uuid: String){
        ivPersonAvatar.setImageDrawable(null)
        setAllTvRvToDeafult()
        resetTvVisibility()
        this.uuid = uuid
        setRV()
        setBindings()
        setCardPersonData()
        setAvatarSpinnerVisibility(true)
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
        if(PersonListFromTree.getPerson(uuid) != null) {
            rvComments.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = RVCommentsPerson(
                    PersonListFromTree.getPerson(uuid)!!.details.comments,
                    context)
            }
        }
    }

    private fun setCardPersonData(){
        val person = PersonListFromTree.getPerson(uuid)
        if (person != null){
            tvPersonName.text = person.details.name
            tvPersonSurname.text = person.details.surname
            tvPersonSex.text =
                    when(person.details.sex){
                        PersonListFromTree.PERSON_FEMALE -> getString(R.string.person_sex_female)
                        PersonListFromTree.PERSON_MALE -> getString(R.string.person_sex_male)
                        else -> getString(R.string.person_sex_undefined)
            }
            if(PersonListFromTree.getPersonDeathDate(person,activity!!) == getString(R.string.no_information)){
                tvDeathDateText.visibility = View.GONE
                tvDeath.visibility = View.GONE
            }else{
                tvDeath.visibility = View.VISIBLE
                tvDeath.text = PersonListFromTree.getPersonDeathDate(person,activity!!)
                tvDeathDateText.visibility = View.VISIBLE
            }
            tvBirth.text = PersonListFromTree.getPersonBirthDate(person,activity!!)
            if(isNetworkOnLine)
                viewModel.getPersonAvatar(uuid)
        }else
            Toasty.error(activity!!,R.string.api_error).show()
    }

    private fun setBindings(){
        ivBtnGallery.onClick {
            openGalleryActivity()
        }
        ivPersonAvatar.onClick {
            if(ContextCompat.checkSelfPermission(context!!,Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
                openStorageForImage()
            else
                requestStoragePermission()
        }
        ibtnAddComment.onClick {
            hideKeyboard()
            if(!etCommentAdd.text.isNullOrBlank()){
                viewModel.addCommentToPerson(uuid,etCommentAdd.text.toString())
                etCommentAdd.text.clear()
            }
        }
        if(rvParents.adapter?.itemCount == 0)
            tvParentsText.visibility = View.INVISIBLE
        if(rvSiblings.adapter?.itemCount == 0)
            tvSiblingsText.visibility = View.INVISIBLE
        if(rvMarriage.adapter?.itemCount == 0)
            tvMarriageText.visibility = View.INVISIBLE
        if(rvChildren.adapter?.itemCount == 0)
            tvChildrenText.visibility = View.INVISIBLE

        this.view!!.setOnTouchListener(object : OnSwipeDetector(context!!){
            override fun onSwipeTop() {
                if(rvParents.adapter?.itemCount != 0){
                    setAllTvRvToDeafult()
                    cardViewPersonData.visibility = View.GONE
                    rvParents.visibility = View.VISIBLE
                }
            }

            override fun onSwipeLeft() {
                if(rvMarriage.adapter?.itemCount != 0) {
                    setAllTvRvToDeafult()
                    setTvMarriageToVertical(false)
                    cardViewPersonData.visibility = View.GONE
                    rvMarriage.visibility = View.VISIBLE
                }
            }

            override fun onSwipeRight() {
                if(rvSiblings.adapter?.itemCount != 0) {
                    setAllTvRvToDeafult()
                    setTvSiblingToVertical(false)
                    cardViewPersonData.visibility = View.GONE
                    rvSiblings.visibility = View.VISIBLE
                }
            }

            override fun onSwipeBottom() {
                if(rvChildren.adapter?.itemCount != 0) {
                    setAllTvRvToDeafult()
                    cardViewPersonData.visibility = View.GONE
                    rvChildren.visibility = View.VISIBLE
                }
            }

            override fun onAnyActionPerformed() {
                hideKeyboard()
                setAllTvRvToDeafult()
            }
        })
        viewModel.avatarURL.observe(this, Observer {
            if(it != null)
                if (viewModel.lastUUID.value == uuid)
                    loadAvatar(it)
            else
                onVMMessage(viewModel.AVATAR_LOAD_FAILED)
            setAvatarSpinnerVisibility(false)
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
        cardViewPersonData.visibility = View.VISIBLE
    }

    private fun resetTvVisibility(){
        tvChildrenText.visibility = View.VISIBLE
        tvMarriageText.visibility = View.VISIBLE
        tvSiblingsText.visibility = View.VISIBLE
        tvParentsText.visibility = View.VISIBLE
    }

    override fun onVMMessage(msg: String?) {
        when(msg){
            viewModel.AVATAR_LOAD_FAILED -> {
                if (viewModel.lastUUID.value == uuid) {
                    Toasty.error(activity!!, R.string.failed_load_avatar).show()
                    setAvatarSpinnerVisibility(false)
                }
            }
            viewModel.AVATAR_LOAD_NO_AVATAR -> {
                if (viewModel.lastUUID.value == uuid) {
                    Toasty.info(activity!!, R.string.no_avatar_load).show()
                    loadNoAvatar()
                    setAvatarSpinnerVisibility(false)
                }
            }
            viewModel.FILE_UPLOAD_FAILED ->{
                setAvatarSpinnerVisibility(false)
                Toasty.error(activity!!,R.string.error_upload).show()
            }
            viewModel.FILE_UPLOAD_SUCCESS -> {
                setAvatarSpinnerVisibility(true)
                viewModel.getPersonAvatar(uuid)
            }
            viewModel.COMMENT_ADDED -> Toasty.success(activity!!, R.string.comment_add_success).show()
            viewModel.ERROR_COMMENT -> Toasty.error(activity!!, R.string.error_upload).show()
        }
    }

    private fun setAvatarSpinnerVisibility(boolean: Boolean){
        when(boolean){
            true -> spinnerAvatarPerson.visibility = View.VISIBLE
            false -> spinnerAvatarPerson.visibility = View.GONE
        }
    }

    private fun loadAvatar(url: String){
        Glide.with(this)
            .load(url)
            .apply(RequestOptions().circleCrop())
            .into(ivPersonAvatar)
    }

    private fun loadNoAvatar(){
        Glide.with(this)
            .load(R.drawable.no_avatar)
            .apply(RequestOptions().circleCrop())
            .into(ivPersonAvatar)
    }

    private fun openStorageForImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, null), CHANGE_PERSON_AVATAR)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            when (requestCode) {
                REQUEST_PERMISSION_STORAGE -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) openStorageForImage()
                    else Toasty.info(ctx, resources.getString(R.string.need_permission)).show()
                }
            }
        }
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_STORAGE
        )
    }

    private fun openGalleryActivity(){
        val intent = Intent(context!!,PersonGalleryActivity::class.java)
        intent.putExtra(Parameters.PERSON_UUID,uuid)
        startActivity(intent)
    }

}