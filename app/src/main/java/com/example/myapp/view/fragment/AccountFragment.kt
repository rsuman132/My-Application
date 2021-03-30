package com.example.myapp.view.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapp.R
import com.example.myapp.view.util.ToastMessage
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.log_out_alert.view.*
import org.w3c.dom.Text


class AccountFragment : Fragment(), View.OnClickListener
        , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var nameText : TextView
    private lateinit var emailText : TextView
    private lateinit var phoneText : TextView
    private lateinit var profileImg : ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navImage : ImageView
    private lateinit var navText : TextView
    private lateinit var navPhone : TextView
    private lateinit var navigationView : NavigationView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameText = view.findViewById(R.id.acc_name)
        emailText = view.findViewById(R.id.acc_email)
        profileImg = view.findViewById(R.id.navHeaderImage)
        phoneText = view.findViewById(R.id.acc_phone)
        toolbar = view.findViewById(R.id.toolbar)

        navigationView = view.findViewById(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0)
        navText = headerView.findViewById(R.id.navEmail)
        navImage = headerView.findViewById(R.id.navHeaderImage)
        navPhone = headerView.findViewById(R.id.navPhone)


        toggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)
        toolbar.setNavigationIcon(R.drawable.ic_menu)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser

        nameText.text = firebaseUser.displayName
        emailText.text = firebaseUser.email

        Glide
            .with(requireActivity())
            .load(firebaseUser.photoUrl)
            .fitCenter()
            .error(R.drawable.ic_image)
            .into(profileImg)

        phoneText.text = firebaseUser.phoneNumber

        navText.text = firebaseUser.email
        navPhone.text = firebaseUser.phoneNumber
        Glide
                .with(requireActivity())
                .load(firebaseUser.photoUrl)
                .fitCenter()
                .error(R.drawable.ic_image)
                .into(navImage)


    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

    override fun onNavigationItemSelected(item: MenuItem) : Boolean{
        when(item.itemId){
            R.id.logout -> logout()
            R.id.delete -> ToastMessage.displayToast(requireContext(), "Deleted")
            R.id.edit -> ToastMessage.displayToast(requireContext(), "Edited")
        }
        return true
    }

    private fun logout(){
        val dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.log_out_alert, null)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(dialogView)
        builder.setCancelable(false)
        val mBuilder = builder.show()
        mBuilder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogView.yes_alert.setOnClickListener {
            mBuilder.dismiss()
            firebaseAuth.signOut()
            ToastMessage.displayToast(requireContext(), "Successfully logged out")
            findNavController().navigate(R.id.action_accountFragment_to_registerFragment)
        }
        dialogView.no_alert.setOnClickListener {
            mBuilder.dismiss()
        }
        dialogView.alert_close.setOnClickListener {
            mBuilder.dismiss()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}