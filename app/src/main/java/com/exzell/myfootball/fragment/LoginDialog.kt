package com.exzell.myfootball.fragment

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.text.toSpannable
import com.exzell.myfootball.R
import com.exzell.myfootball.databinding.DialogSignInBinding
import com.exzell.myfootball.viewmodel.SplashViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class LoginDialog(context: Context, action: String, private val mViewModel: SplashViewModel) : BottomSheetDialog(context) {
    companion object {

        const val ACTION_SIGN_UP = "Sign up"
        const val ACTION_SIGN_IN = "Sign in"
    }

    private var toSignIn = action.equals(ACTION_SIGN_IN)

    private var mBinding: DialogSignInBinding? = null

    var onLoginDone: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DialogSignInBinding.inflate(LayoutInflater.from(context))

        onViewCreated()

        setContentView(mBinding!!.root)
    }


    private fun onViewCreated() {

        with(mBinding!!) {

            buttonSign.apply {
                setText(if (toSignIn) R.string.sign_in else R.string.sign_up)


                val onSuccess = {
                    onLoginDone?.invoke()

                    mViewModel.changeAutoLoginState(checkboxRemember.isChecked)

                    dismiss()
                }

                val onError = { message: String ->
                    textSignMessage.text = message
                    textSignMessage.visibility = View.VISIBLE
                    indicatorSign.visibility = View.GONE
                    layoutControl.visibility = View.VISIBLE
                }


                setOnClickListener {
                    indicatorSign.visibility = View.VISIBLE
                    layoutControl.visibility = View.GONE

                    if (toSignIn) mViewModel.signin(editEmail.text.toString(), editPassword.text.toString(), onSuccess, onError)
                    else mViewModel.signup(editEmail.text.toString(), editPassword.text.toString(), onSuccess, onError)
                }
            }

            textForgotPassword.setOnClickListener {
                if(editEmail.text!!.isEmpty()){
                    Toast.makeText(context, "The email should not be empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                mViewModel.retrievePassword(editEmail.text.toString())
            }

            if(toSignIn) highlightPartOfText()

            checkboxRemember.isChecked = mViewModel.canRemember()
        }
    }

    private fun highlightPartOfText(){
        mBinding!!.textSignUpLink.apply {
            visibility = View.VISIBLE

            val clickPart = context.getString(R.string.sign_up)

            val index = text.indexOf(clickPart)

            val clickableSpan = object: ClickableSpan(){
                override fun onClick(widget: View) {
                    toSignIn = false
                    mBinding!!.buttonSign.setText(clickPart)
                    visibility = View.GONE
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = context.getColor(R.color.blue)
                }
            }

            val spanString = text.toSpannable().apply {
                setSpan(clickableSpan, index, index+clickPart.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            setText(spanString)
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
}