package com.monicatifanyz.digimagz.view.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsAdapter
import com.monicatifanyz.digimagz.adapter.RecyclerViewVideoAdapter
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.NewsModel
import com.monicatifanyz.digimagz.model.VideoModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_video.*
import kotlinx.android.synthetic.main.fragment_video.view.*
import java.util.ArrayList

class VideoFragment : Fragment() {

    lateinit var initRetrofit : InitRetrofit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_video, container, false)

        initRetrofit = InitRetrofit()

        setRecyclerView()

        view.swipeRefreshLayout.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({
                setRecyclerView()
                view.swipeRefreshLayout.isRefreshing = false
            }, 2500)
        }

        return view
    }

    private fun setRecyclerView() {
        initRetrofit.getVideoFromApi()
        initRetrofit.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (arrayList!!.isNotEmpty()) {
                    Log.e("videoData", arrayList.size.toString())
                    showRecyclerViewVideo(arrayList as ArrayList<VideoModel>)
                }
            }

        })
    }

    fun showRecyclerViewVideo(list: ArrayList<VideoModel>) {
        recyclerViewVideo.layoutManager = LinearLayoutManager(context)
        val recyclerViewVideoAdapter = RecyclerViewVideoAdapter(list, context!!)
        recyclerViewVideo.adapter = recyclerViewVideoAdapter
        recyclerViewVideoAdapter.notifyDataSetChanged()
        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = View.GONE
    }

    fun scrollUp() {
        nestedScrollViewVideo.smoothScrollTo(0,0)
    }

    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmer()
    }

    override fun onPause() {
        shimmer_view_container.stopShimmer()
        super.onPause()
    }
}
