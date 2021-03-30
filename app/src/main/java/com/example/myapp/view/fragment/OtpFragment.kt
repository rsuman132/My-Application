package com.example.myapp.view.fragment


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapp.R
import com.example.myapp.view.util.Constant.Companion.ONE_MINUTE_TIMER
import com.example.myapp.view.util.CustomProgressDialog
import com.example.myapp.view.util.ToastMessage
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.*
import com.mukesh.OtpView
import kotlinx.android.synthetic.main.otp_back_alert.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class OtpFragment : Fragment(), View.OnClickListener{

    private lateinit var counter : TextView
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var sendAgain : TextView
    private lateinit var otpBack : ImageView
    private lateinit var myPhone : TextView
    private lateinit var verifyOtp : Button
    private lateinit var otpView : OtpView
    private lateinit var changePhone : TextView
    private val args : OtpFragmentArgs by navArgs()

    private lateinit var firebaseAuth: FirebaseAuth
    private val progressDialog = CustomProgressDialog()

    var callback: OnVerificationStateChangedCallbacks? = null

    private lateinit var verificationId : String
    private lateinit var token : ForceResendingToken

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog.show(requireContext())

        firebaseAuth = FirebaseAuth.getInstance()

        counter = view.findViewById(R.id.counter)
        sendAgain = view.findViewById(R.id.send_again)
        otpBack = view.findViewById(R.id.otp_back)
        myPhone = view.findViewById(R.id.my_name)
        verifyOtp = view.findViewById(R.id.verify_otp_btn)
        otpView = view.findViewById(R.id.otpView)
        changePhone = view.findViewById(R.id.otp_phone)


        coundownTimer()
        sendAgain.setOnClickListener(this)
        otpBack.setOnClickListener(this)
        changePhone.setOnClickListener(this)
        verifyOtp.setOnClickListener(this)

        myPhone.text = args.phoneNumber


        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                progressDialog.dialog.dismiss()
                findNavController().navigate(R.id.action_registerFragment_to_accountFragment)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                progressDialog.dialog.dismiss()
                requireActivity().onBackPressed()
            }

            override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
                progressDialog.dialog.dismiss()
                this@OtpFragment.verificationId = verificationId
                this@OtpFragment.token = token
            }
        }

        getOtp()

    }

    private fun getOtp() {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(args.phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callback)
            .build()
        verifyPhoneNumber(options)
    }

    private fun coundownTimer() {
        countDownTimer = object : CountDownTimer(ONE_MINUTE_TIMER, 1000){
            override fun onFinish() {
                stopCountdown()
            }

            override fun onTick(millisUntilFinished: Long) {
                ONE_MINUTE_TIMER = millisUntilFinished
                val minute = (ONE_MINUTE_TIMER / 1000) / 60
                val seconds = (ONE_MINUTE_TIMER / 1000) % 60

                val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minute, seconds)
                counter.text = formattedTime
            }

        }
        countDownTimer.start()
    }

    override fun onClick(v: View?) {
      when(v?.id){
          R.id.send_again -> {
              startCountdown()
              resendOtp(token)
          }
          R.id.otp_back -> {
              backpressed()
          }
          R.id.otp_phone -> {
              requireActivity().onBackPressed()
          }
          R.id.verify_otp_btn ->{
              registerOTP(verificationId)
          }
      }
    }

    private fun backpressed() {
        val dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.otp_back_alert, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(dialogView)
        builder.setCancelable(false)
        val mBuilder = builder.show()
        mBuilder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.alert_do_it.setOnClickListener {
            mBuilder.dismiss()
            requireActivity().onBackPressed()
        }
        dialogView.alert_ok.setOnClickListener {
            mBuilder.dismiss()
        }
    }

    private fun resendOtp(token : ForceResendingToken) {
        progressDialog.show(requireContext())
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(requireActivity())
                .setCallbacks(callback)
                .setPhoneNumber(args.phoneNumber)
                .setForceResendingToken(token)
                .build()
        verifyPhoneNumber(options)
        Handler().postDelayed({
            progressDialog.dialog.dismiss()
        }, 500)
        ToastMessage.displayToast(requireContext(), resources.getString(R.string.resend_otp))
    }

    private fun registerOTP(verificationId : String) {

        otpView.setOtpCompletionListener {
                otp -> ToastMessage.displayToast(requireContext(), otp)
        }
        val otp = otpView.text.toString().trim()
        when {
            otp.isEmpty() -> {
                Snackbar.make(requireView(), "Please enter your received OTP", Snackbar.LENGTH_SHORT).show()
                return
            }
            otp.length > 6 -> {
                Snackbar.make(requireView(), "Please enter your valid OTP", Snackbar.LENGTH_SHORT).show()
                return
            }
            else -> {
                val credential : PhoneAuthCredential = getCredential(verificationId, otp)
                progressDialog.show(requireContext())
                signInWithPhoneAuthCredential(credential)
                stopCountdown()
            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    progressDialog.dialog.dismiss()
                    findNavController().navigate(R.id.action_otpFragment_to_accountFragment)
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        progressDialog.dialog.dismiss()
                        ToastMessage.displayToast(requireContext(), "Invalid OTP")
                    }
                }
            }
    }

    //countdown timer function
    private fun startCountdown(){
        countDownTimer.start()
        sendAgain.visibility = View.INVISIBLE
        counter.visibility = View.VISIBLE
    }

    private fun stopCountdown(){
        countDownTimer.cancel()
        counter.visibility = View.INVISIBLE
        sendAgain.visibility = View.VISIBLE
    }
    //countdown timer function
}