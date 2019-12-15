package com.monicatifanyz.digimagz.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.api.InitRetrofit
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    lateinit var initRetrofit: InitRetrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)
        initRetrofit = InitRetrofit()
        initRetrofit.getNewsFromApi()

        return view
    }

    fun scrollUp() {
        nestedScrollViewSearch.smoothScrollTo(0,0)
    }
}
