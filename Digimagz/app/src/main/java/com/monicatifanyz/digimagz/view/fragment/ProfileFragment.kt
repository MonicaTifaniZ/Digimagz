package com.monicatifanyz.digimagz.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        view.materialButtonSignOut.setOnClickListener {
            signOut()
        }
        return view
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(context!!)
            .addOnCompleteListener {
                toMain()
            }
            .addOnFailureListener {
                Log.e("ErrorSignOut", it.message.toString())
            }
    }

    private fun toMain() {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}

