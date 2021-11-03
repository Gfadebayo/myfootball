package com.exzell.myfootball.fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.core.content.ContentResolverCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.exzell.myfootball.FootballApplication
import com.exzell.myfootball.R
import com.exzell.myfootball.databinding.FragmentProfileBinding
import com.exzell.myfootball.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import timber.log.Timber
import java.security.Permission

class ProfileFragment : Fragment() {
    private var mBinding: FragmentProfileBinding? = null
    private lateinit var mViewModel: ProfileViewModel
    private var mResultLauncher:  ActivityResultLauncher<Array<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = requireActivity().application as FootballApplication
        mViewModel = ViewModelProvider(this, NewInstanceFactory())
                .get(ProfileViewModel::class.java)

        app.appComponent.injectRepo(mViewModel)
        app.appComponent.injectAuth(mViewModel)

        mResultLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { result: Uri ->
            mViewModel.updateProfilePic(result) { isSuccessful: Boolean ->
                if (isSuccessful) {
                    requireContext().contentResolver
                            .takePersistableUriPermission(result, Intent.FLAG_GRANT_READ_URI_PERMISSION and Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                    loadImage(result)
                } else Toast.makeText(requireContext(), "Picture update failed", Toast.LENGTH_SHORT).show()
            }
        }

        registerForActivityResult(ActivityResultContracts.OpenDocument()){
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentProfileBinding.inflate(inflater)
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }

        mBinding!!.apply {
            textName.text = getString(R.string.text_field_name, mViewModel.getDisplayName())
            textClickAction(textName, R.string.text_field_name, dialog)

            textMail.text = getString(R.string.text_field_email, mViewModel.getEmail())
            textClickAction(textMail, R.string.text_field_email, dialog)

            textPhone.text = getString(R.string.text_field_phone, /*mViewModel.getPhone()*/"08146291279")
            textClickAction(textPhone, R.string.text_field_phone, dialog)

            Timber.d("Profile picture url is %s", mViewModel.getProfilePath().toString())
            loadImage(mViewModel.getProfilePath())
            imageChangeProfile.setOnClickListener { v: View? -> mResultLauncher!!.launch(arrayOf("image/*")) }
        }
    }

    private fun textClickAction(tv: MaterialTextView, titleRes: Int, dialog: MaterialAlertDialogBuilder){
        val editText = EditText(requireContext())

        tv.setOnClickListener {
            dialog.setView(editText)

            dialog.setPositiveButton(R.string.update) { _, _ ->
                val listener = { isSuccessful: Boolean ->

                    if(isSuccessful) tv.text = getString(titleRes, editText.text.toString())
                    else Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }

                when (tv.id){
                    mBinding!!.textName.id -> mViewModel.updateDisplayName(editText.text.toString(), listener)

                    mBinding!!.textMail.id -> mViewModel.updateEmail(editText.text.toString(), listener)

                    mBinding!!.textPhone.id -> mViewModel.updatePhone(editText.text.toString(), listener)
                }
            }

            dialog.show().setOnShowListener {
                editText.setText(tv.text.split("\\n".toRegex())[1])
            }
        }
    }

    private fun loadImage(photoUrl: Uri) {
        val uri = requireContext().contentResolver.openInputStream(photoUrl)
        requireContext().contentResolver.openInputStream(photoUrl)?.let {

            Timber.d("Profile url is %s", photoUrl.toString())
            Glide.with(this)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .centerInside()
                    .circleCrop()
                    .into(mBinding!!.imageProfile)
                    .request!!.apply {
                        if(!isRunning) begin()
                    }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mResultLauncher!!.unregister()
        mResultLauncher = null
        mBinding = null
    }
}