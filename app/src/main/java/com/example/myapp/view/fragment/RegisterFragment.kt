package com.example.myapp.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import com.example.myapp.R
import com.example.myapp.view.util.Constant.Companion.RC_SIGN_IN
import com.example.myapp.view.util.CustomProgressDialog
import com.example.myapp.view.util.HideKeyBoard
import com.example.myapp.view.util.ToastMessage
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import com.google.firebase.FirebaseApp
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.center_line.*
import kotlinx.android.synthetic.main.fragment_register.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class RegisterFragment : Fragment(), View.OnClickListener, View.OnTouchListener{

    private lateinit var parentLL : LinearLayout
    private lateinit var registerHelp : TextView
    private lateinit var registerButton : Button
    private lateinit var googleImage : Button
    private lateinit var fbImage : Button
    private lateinit var fullName : Button
    private lateinit var ccp : CountryCodePicker
    private lateinit var phoneNumber : EditText
    private lateinit var sign_in_head : TextView
    private lateinit var registerScroll : NestedScrollView
    private lateinit var registerChildLinearLayout: LinearLayout
    private var behaviour : BottomSheetBehavior<LinearLayout>? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient
    private var callbackManager: CallbackManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.sdkInitialize(requireContext())

        parentLL = view.findViewById(R.id.register_parent_ll)
        registerHelp = view.findViewById(R.id.sign_in_forget_password)
        registerButton = view.findViewById(R.id.sign_in_btn)
        googleImage = view.findViewById(R.id.sign_in_google)
        fbImage = view.findViewById(R.id.sign_in_facebook)
        fullName = view.findViewById(R.id.register_fName)
        ccp = view.findViewById(R.id.sign_in_ccp)
        phoneNumber = view.findViewById(R.id.sign_in_phone)
        registerScroll = view.findViewById(R.id.registerScroll)
        registerChildLinearLayout = view.findViewById(R.id.registerChildLL)
        sign_in_head = view.findViewById(R.id.sign_in_head)

        ccp.registerCarrierNumberEditText(phoneNumber)


        behaviour = from(registerChildLinearLayout).apply {
            peekHeight = 750
            behaviour?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        registerHelp.text = resources.getString(R.string.need_help)

        firebaseAuth = FirebaseAuth.getInstance()
        FirebaseApp.initializeApp(requireContext())



        //google register
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)


        //onclick
        parentLL.setOnClickListener(this)
        googleImage.setOnClickListener(this)
        fbImage.setOnClickListener(this)
        registerButton.setOnClickListener(this)

        //on touch
        registerScroll.setOnTouchListener(this)
        ccp.setOnTouchListener(this)
        phoneNumber.setOnTouchListener(this)
        sign_in_head.setOnTouchListener(this)
        sign_in_body.setOnTouchListener(this)
        center_line.setOnTouchListener(this)

    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.register_parent_ll -> {
                HideKeyBoard.hideSoftKeyBoard(requireContext(), v)
            }
            R.id.sign_in_google -> {
                signIn()
            }
            R.id.sign_in_facebook -> {
                LoginManager.getInstance().logInWithReadPermissions(requireActivity(), listOf("email", "public_profile"))
                LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
                    override fun onSuccess(loginResult: LoginResult) {
                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                        ToastMessage.displayToast(requireContext(), "facebook:onCancel")
                    }

                    override fun onError(error: FacebookException?) {
                        ToastMessage.displayToast(requireContext(), "facebook:onError")
                    }

                })
            }
            R.id.sign_in_btn -> {
                generateOTP()
            }
        }
    }

    private fun generateOTP() {
        val phWithCCP = ccp.fullNumberWithPlus.toString()
        val phone = phoneNumber.text.toString().trim()
        if (TextUtils.isEmpty(phone)) {
            phoneNumber.error = resources.getString(R.string.blankPhonenumber)
            phoneNumber.requestFocus()
            return
        } else if (phone.length != 11){
            phoneNumber.error = resources.getString(R.string.validPhonenumber)
            phoneNumber.requestFocus()
            return
        }
        else {
            phoneNumber.error = null
        }

        val action = RegisterFragmentDirections.actionRegisterFragmentToOtpFragment(phWithCCP)
        findNavController().navigate(action)

    }


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(v?.id){
            R.id.registerScroll -> {
                HideKeyBoard.hideSoftKeyBoard(requireContext(), v)
                return false
            }
            R.id.sign_in_phone -> {
                behaviour?.state = BottomSheetBehavior.STATE_EXPANDED
                val im = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.showSoftInput(requireView(), 0)
                return false
            }
        }
        return true
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful){
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    ToastMessage.displayToast(requireContext(), "$e")
                }
            } else {

            }

        }
    }

    //google login
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_registerFragment_to_accountFragment)
                } else {
                    ToastMessage.displayToast(requireContext(), "Failed")
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    updateUI(user)
                } else {
                    ToastMessage.displayToast(requireContext(), "Authentication failed.")
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user !=null){
            findNavController().navigate(R.id.action_registerFragment_to_accountFragment)
        }

    }

    override fun onStart() {
        val user = firebaseAuth.currentUser
        super.onStart()
        updateUI(user)
    }
}