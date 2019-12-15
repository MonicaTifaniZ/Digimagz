package com.monicatifanyz.digimagz.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.UserModel
import com.monicatifanyz.digimagz.view.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProfileFragment : Fragment() {

    private lateinit var initRetrofit: InitRetrofit
    private val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        initRetrofit = InitRetrofit()
        initRetrofit.getUserFromApi(firebaseUser?.email.toString())
        initRetrofit.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess {
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (arrayList!!.isNotEmpty()) {
                    var arrayListUser: ArrayList<UserModel> = ArrayList<UserModel>()
                    arrayListUser = arrayList.clone() as ArrayList<UserModel>

                    Glide.with(activity!!)
                        .load(arrayListUser[0].urlPic)
                        .placeholder(R.color.chef)
                        .into(imgProfile)

                    tvNameProfile.text = arrayListUser[0].userName
                    if(arrayListUser[0].gender.equals("L")) {
                        tvGender.text = "Laki-laki"
                    } else {
                        tvGender.text = "Perempuan"
                    }

                    val format = SimpleDateFormat("yyyy-MM-dd", Locale("in", "ID"))
                    val date = format.parse(arrayListUser[0].dateBirth)
                    val dateFormatterText = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))

                    tvAge.text = dateFormatterText.format(date.time)
                    tvEmailProfile.text = arrayListUser[0].email
                    tvStatus.text = arrayListUser[0].userType
                }
            }

        })

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

