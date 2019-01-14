package io.github.luteoos.gent.view.fragment

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.luteoos.kotlin.mvvmbaselib.BaseFragmentMVVM
import es.dmoral.toasty.Toasty
import io.github.luteoos.gent.R
import io.github.luteoos.gent.session.SessionManager
import io.github.luteoos.gent.utils.UriResolver
import io.github.luteoos.gent.viewmodels.MyProfileFragmentViewModel
import kotlinx.android.synthetic.main.fragment_my_profile.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx

class MyProfileFragment : BaseFragmentMVVM<MyProfileFragmentViewModel>() {

    private val REQUEST_PERMISSION_STORAGE = 2137
    private val CHANGE_AVATAR = 1944

    override fun getLayoutID(): Int = R.layout.fragment_my_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        viewModel = MyProfileFragmentViewModel()
        this.connectToVMMessage()
        setBindings()
        initialize()
    }

    override fun onVMMessage(msg: String?) {
        when(msg){
            viewModel.AVATAR_LOAD_NO_AVATAR -> {
                avatarProgressBar.visibility = View.GONE
                tvErrorAvatar.visibility = View.GONE
                tvNoAvatar.visibility = View.VISIBLE
            }
            viewModel.AVATAR_LOAD_FAILED -> {
                tvErrorAvatar.visibility = View.VISIBLE
                avatarProgressBar.visibility = View.GONE
                avatarProgressBar.visibility = View.GONE
            }
            viewModel.FILE_UPLOAD_SUCCESS -> reloadAvatar()
            viewModel.FILE_UPLOAD_FAILED -> {
                avatarProgressBar.visibility = View.GONE
                Toasty.error(context!!, R.string.error_upload).show()
            }
        }
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

    private fun initialize(){
        tvUsername?.text = SessionManager.username
        if(isNetworkOnLine)
            viewModel.getUserAvatarByte()
        else {
            tvErrorAvatar.visibility = View.VISIBLE
            avatarProgressBar.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CHANGE_AVATAR -> {
                    avatarProgressBar.visibility = View.VISIBLE
                    val uri = data?.data
                    val file = UriResolver.getFileFromUri(uri!!, context!!.contentResolver)
                    viewModel.uploadMediaToServer(file)
                }
            }
        }
    }

    private fun  setBindings(){
        btnChangeAvatar.onClick {
            if(ContextCompat.checkSelfPermission(context!!,Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
                openStorageForImage()
            else
                requestStoragePermission()
        }
        btnLogout.onClick {
            SessionManager.logout(context!!)
        }
        viewModel.avatarByteString.observe(this, Observer {
            if(it != null){
                loadAvatar(it)
                avatarProgressBar.visibility = View.GONE
                tvErrorAvatar.visibility = View.GONE
                avatarProgressBar.visibility = View.GONE
            }
        })
    }

    private fun openStorageForImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, null), CHANGE_AVATAR)
    }

    private fun loadAvatar(bytes: String){
        val byteArray = Base64.decode(bytes, Base64.DEFAULT)
        Glide.with(this)
            .asBitmap()
            .load(byteArray)
            .apply(RequestOptions().circleCrop())
            .into(iv_profile_pic)
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_STORAGE
        )
    }

    private fun reloadAvatar(){
        avatarProgressBar.visibility = View.VISIBLE
        if(isNetworkOnLine)
            viewModel.getUserAvatarByte()
        else {
            tvErrorAvatar.visibility = View.VISIBLE
            avatarProgressBar.visibility = View.GONE
        }
    }
}