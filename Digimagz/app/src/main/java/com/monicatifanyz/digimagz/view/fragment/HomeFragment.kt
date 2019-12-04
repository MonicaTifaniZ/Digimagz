package com.monicatifanyz.digimagz.view.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.monicatifanyz.digimagz.R


class HomeFragment : Fragment() {

    private val recyclerViewTranding : RecyclerView? = null
    private val recyclerViewCoverStory : RecyclerView? = null
    private val recyclerViewNews : RecyclerView? = null
    private val shimmerFrameLayoutTranding: FrameLayout?=null
    private val shimmerFrameCoverStory: FrameLayout?=null
    private val shimmerFrameLayoutNews: FrameLayout?=null
    private val shimmerFrameLayoutSlider: FrameLayout?=null
    private val materialButtonMoreTrending:MaterialButton?=null
    private val nestedScrollViewHome: NestedScrollView? = null
    private val swipeRefreshLayout: SwipeRefreshLayout? =null

    private val relativeLayout:RelativeLayout?=null
    private val mPager: ViewPager? = null
    private val currentPage: Int = 0
    private val NUM_PAGES : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}

