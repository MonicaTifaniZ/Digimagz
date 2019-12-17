package com.monicatifanyz.digimagz.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.view.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val rcSignIn = 9001

    private lateinit var initRetrofit:InitRetrofit

    private lateinit var idpConfigList: List<AuthUI.IdpConfig>
    private lateinit var authMethodPickerLayout: AuthMethodPickerLayout

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private lateinit var fragmentManager: FragmentManager

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem

    private var doubleBackToExit: Boolean = false
    private var checkFragment: Boolean = false

    private lateinit var fragmentActive: Fragment
    private lateinit var fragmentHome: Fragment
    private lateinit var fragmentVideo: Fragment
    private lateinit var fragmentSearch: Fragment
    private lateinit var fragmentEmagz: Fragment
    private lateinit var fragmentProfile: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRetrofit = InitRetrofit()
        fragmentManager = supportFragmentManager
        fragmentHome = HomeFragment()
        fragmentVideo = VideoFragment()
        fragmentSearch = SearchFragment()
        fragmentEmagz = EmagzFragment()
        fragmentProfile = ProfileFragment()
        fragmentActive = fragmentHome

        setIdpConfigList()
        authMethodPickerLayout = AuthMethodPickerLayout
            .Builder(R.layout.activity_login)
            .setGoogleButtonId(R.id.materialButtonGoogle)
            .setFacebookButtonId(R.id.materialButtonFacebook)
            .setEmailButtonId(R.id.materialButtonEmail)
            .build()

        doubleBackToExit = true
        checkFragment = false

        menu = bottomNavigationView.menu
        menuItem = menu.findItem(R.id.login_profile_menu)
        if (firebaseUser != null) {
            menuItem.title = "Profile"
        } else {
            menuItem.isCheckable = false
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(object :BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.home_menu -> {
                        fragmentManager.beginTransaction().hide(fragmentActive).show(fragmentHome)
                            .commit()
                        if (fragmentActive === fragmentHome) {
                            (fragmentHome as HomeFragment).scrollUp()
                        }
                        fragmentActive = fragmentHome
                        doubleBackToExit = true
                        return true
                    }
                    R.id.video_menu -> {
                        fragmentManager.beginTransaction().hide(fragmentActive).show(fragmentVideo)
                            .commit()
                        if (fragmentActive === fragmentVideo) {
                            (fragmentVideo as VideoFragment).scrollUp()
                        }
                        fragmentActive = fragmentVideo
                        doubleBackToExit = false
                        return true
                    }
                    R.id.explore_menu -> {
                        fragmentManager.beginTransaction().hide(fragmentActive).show(fragmentSearch)
                            .commit()
                        if (fragmentActive === fragmentSearch) {
                            (fragmentSearch as SearchFragment).scrollUp()
                        }
                        fragmentActive = fragmentSearch
                        doubleBackToExit = false
                        return true
                    }
                    R.id.login_profile_menu -> {
                        if (firebaseUser != null) {
                            fragmentManager.beginTransaction().hide(fragmentActive)
                                .show(fragmentProfile).commit()
                            fragmentActive = fragmentProfile
                            doubleBackToExit = false
                        } else {
                            showSignInOption()
                        }
                        return true
                    }
                }
                return false
            }
        })

        fragmentManager.beginTransaction().add(R.id.frameLayoutForFragment, fragmentProfile, "Profile").hide(fragmentProfile).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayoutForFragment, fragmentEmagz, "Emagz").hide(fragmentEmagz).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayoutForFragment, fragmentSearch, "Search").hide(fragmentSearch).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayoutForFragment, fragmentVideo, "Video").hide(fragmentVideo).commit()
        fragmentManager.beginTransaction().add(R.id.frameLayoutForFragment, fragmentHome, "Home").commit()
    }

    private fun setIdpConfigList() {
        idpConfigList = arrayListOf(
            GoogleBuilder().build(),
            FacebookBuilder().build(),
            EmailBuilder().build()

        )
    }

    fun showSignInOption() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(idpConfigList)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.AppTheme)
                .setAuthMethodPickerLayout(authMethodPickerLayout)
                .build(),
            MainActivity().rcSignIn
        )
    }

    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == MainActivity().rcSignIn) {
            // Successfully signed in
            if (resultCode == Activity.RESULT_OK) {
                val firebaseUser = FirebaseAuth.getInstance().currentUser
                Log.e("Email", firebaseUser!!.email.toString())
                Log.e("Name", firebaseUser.displayName.toString())
                Log.e("Phone", firebaseUser.phoneNumber.toString())
                Log.e("Photo", firebaseUser.photoUrl.toString())
                Log.e("Privider Id", firebaseUser.providerId)
                Log.e("U Id", firebaseUser.uid)
                initRetrofit.postUserToApi(
                    firebaseUser.email.toString(),
                    firebaseUser.displayName.toString(),
                    firebaseUser.photoUrl.toString()
                )
                toMain()
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        }
        doubleBackToExit = true
        fragmentManager.beginTransaction().hide(fragmentActive).show(HomeFragment()).commit()
        fragmentActive = fragmentHome
        bottomNavigationView.selectedItemId = R.id.home_menu
    }
}
