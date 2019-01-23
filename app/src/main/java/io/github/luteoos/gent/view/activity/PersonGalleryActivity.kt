package io.github.luteoos.gent.view.activity

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.luteoos.kotlin.mvvmbaselib.BaseActivityMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.network.api.response.MediaDtoResponse
import io.github.luteoos.gent.utils.OnSwipeDetector
import io.github.luteoos.gent.utils.Parameters
import io.github.luteoos.gent.utils.UriResolver
import io.github.luteoos.gent.view.recyclerviews.RVGalleryPerson
import io.github.luteoos.gent.viewmodels.PersonGalleryViewModel
import kotlinx.android.synthetic.main.activity_person_gallery.*
import kotlinx.android.synthetic.main.fragment_person_card.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx

class PersonGalleryActivity: BaseActivityMVVM<PersonGalleryViewModel>() {

    private val ADD_PERSON_MEDIA = 211
    private val REQUEST_PERMISSION_STORAGE = 112
    private lateinit var uuid: String

    override fun getLayoutID(): Int = R.layout.activity_person_gallery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = PersonGalleryViewModel()
        uuid = intent.getStringExtra(Parameters.PERSON_UUID)
        this.connectToVMMessage()
        setBindings()
        viewModel.getMediaList(uuid)
    }

    override fun onVMMessage(msg: String?) {
        when(msg){
            viewModel.FILE_UPLOAD_SUCCESS -> {
                setGallerySpinnerVisibility(false)
                viewModel.getMediaList(uuid)
            }
            viewModel.FILE_UPLOAD_FAILED -> {
                setGallerySpinnerVisibility(false)
                Toasty.error(this, R.string.error_upload).show()
            }
            viewModel.GALLERY_LOAD_FAILED -> {
                setGallerySpinnerVisibility(false)
                Toasty.error(this, R.string.api_error).show()
            }
        }
    }

    private fun setRV(list: MutableList<MediaDtoResponse>){
        setGallerySpinnerVisibility(false)
        rvGallery.apply {
            layoutManager = GridLayoutManager(this@PersonGalleryActivity,2)
            adapter = RVGalleryPerson(list, this@PersonGalleryActivity)
        }
    }

    private fun openStorageForImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, null), ADD_PERSON_MEDIA)
    }

    private fun setBindings(){
        rvGallery.setOnTouchListener(object : OnSwipeDetector(this){
            override fun onSwipeBottom() {
                setGallerySpinnerVisibility(true)
                viewModel.getMediaList(uuid)
            }
        })
        fabAddPhoto.onClick {
            if(ContextCompat.checkSelfPermission(ctx,Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
                openStorageForImage()
            else
                requestStoragePermission()
        }
        viewModel.mediaList.observe(this, Observer {
            if(it != null)
                setRV(it)
            else
                onVMMessage(viewModel.GALLERY_LOAD_FAILED)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            when (requestCode) {
                REQUEST_PERMISSION_STORAGE -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) openStorageForImage()
                    else Toasty.info(this, resources.getString(R.string.need_permission)).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_PERSON_MEDIA -> {
                    setGallerySpinnerVisibility(true)
                    val uri = data?.data
                    val file = UriResolver.getFileFromUri(uri!!, this.contentResolver)
                    viewModel.uploadFileToApi(file,uuid)
                }
            }
        }
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_STORAGE
        )
    }

    private fun setGallerySpinnerVisibility(boolean: Boolean){
        when(boolean){
            true -> spinnerGallery.visibility = View.VISIBLE
            false -> spinnerGallery.visibility = View.GONE
        }
    }
}