package com.monicatifanyz.digimagz.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.monicatifanyz.digimagz.R
import com.monicatifanyz.digimagz.adapter.ImageSliderAdapter
import com.monicatifanyz.digimagz.adapter.RecyclerViewNewsAdapter
import com.monicatifanyz.digimagz.adapter.RecyclerViewStoryAdapter
import com.monicatifanyz.digimagz.api.InitRetrofit
import com.monicatifanyz.digimagz.model.NewsModel
import com.monicatifanyz.digimagz.model.StoryModel
import com.monicatifanyz.digimagz.view.activity.ListNewsActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var initRetrofitTrending: InitRetrofit
    private lateinit var initRetrofitNews : InitRetrofit
    private lateinit var initRetrofitStory : InitRetrofit
    private lateinit var initRetrofitSlider : InitRetrofit

    private var currentPage = 0
    private var NUM_PAGES = 0

    private var swiper : Handler = Handler()
    private var swipeRunnable : Runnable = object : Runnable {
        override fun run() {
            if (currentPage == NUM_PAGES){
                currentPage = 0
            }
            pagerSlider.setCurrentItem(currentPage++, true)

            swiper.postDelayed(this, 3000)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initRetrofitSlider = InitRetrofit()
        initRetrofitTrending = InitRetrofit()
        initRetrofitStory = InitRetrofit()
        initRetrofitNews = InitRetrofit()

        view.materialButtonMoreTrending.setOnClickListener {
            val intent = Intent(context, ListNewsActivity::class.java)
            intent.putExtra("params", "trending")
            startActivity(intent)
        }

        view.swipeRefreshLayout.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({
                setRecyclerView()
                swipeRefreshLayout.isRefreshing = false
            }, 2500)
        }

        return view
    }

    private fun setRecyclerView() {
        swiper.removeCallbacks(swipeRunnable)

        initRetrofitSlider.getSliderFromApi()
        initRetrofitSlider.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (!isAdded) return
                if (arrayList != null) {
                    if (arrayList.isNotEmpty()) {
                        Log.i("Size", arrayList.size.toString())
                        showSlider(arrayList as ArrayList<NewsModel>)
                    } else {
                        Log.i("Size", arrayList.size.toString())
                    }
                }
            }

        })

        initRetrofitTrending.getNewsTrendingFromApi()
        initRetrofitTrending.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (!isAdded) return
                if (arrayList != null) {
                    if (arrayList.isNotEmpty()) {
                        Log.i("Size", arrayList.size.toString())
                        showRecyclerListViewTrending(arrayList as ArrayList<NewsModel>)
                        materialButtonMoreTrending.visibility = View.VISIBLE
                    } else {
                        Log.i("Size", arrayList.size.toString())
                    }
                }
            }

        })

        initRetrofitStory.getStoryFromApi()
        initRetrofitStory.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (!isAdded) return
                if  (arrayList != null)  {
                    if (arrayList.isNotEmpty()) {
                        Log.i("SizeStory", arrayList.size.toString())
                        showRecyclerListViewCoverStory(arrayList as ArrayList<StoryModel>)
                    } else {
                        Log.i("Size", arrayList.size.toString())
                    }
                }
            }
        })

        initRetrofitNews.getNewsFromApi()
        initRetrofitNews.setOnRetrofitSuccess(object : InitRetrofit.OnRetrofitSuccess{
            override fun onSuccessGetData(arrayList: ArrayList<*>?) {
                if (!isAdded) return
                if (arrayList != null) {
                    if (arrayList.isNotEmpty()) {
                        Log.i("Size", arrayList.size.toString())
                        showRecyclerListViewNews(arrayList as ArrayList<NewsModel>)
                    } else {
                        Log.i("Size", arrayList.size.toString())
                    }
                }
            }

        })
    }

    private fun showSlider(arrayList: ArrayList<NewsModel>) {
        if (arrayList.size > 0) {
            pagerSlider.setAdapter(ImageSliderAdapter(arrayList))
            indicatorSlider.setViewPager(pagerSlider)
            val density = resources.displayMetrics.density
            //Set circle indicator radius
            indicatorSlider.radius = 5 * density
            HomeFragment().NUM_PAGES = arrayList.size
            // Auto start of viewpager
            swiper.postDelayed(swipeRunnable, 3000)
            /**Timer swipeTimer = new Timer();
             * swipeTimer.schedule(new TimerTask() {
             * @Override
             * public void run() {
             * handler.post(Update);
             * }
             * }, 3000, 3000); */
            indicatorSlider.setOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    HomeFragment().currentPage = position
                    Log.e("pos", HomeFragment().currentPage.toString())
                }

                override fun onPageSelected(position: Int) {}
                override fun onPageScrollStateChanged(state: Int) {}
            })

            relativeLayoutSlider.visibility = View.VISIBLE
            shimmerFrameLayoutSlider.stopShimmer()
            shimmerFrameLayoutSlider.visibility = View.GONE
        }
    }

    private fun showRecyclerListViewTrending(arrayList: ArrayList<NewsModel>) {
        recylerViewTranding.setHasFixedSize(true)
        recylerViewTranding.layoutManager = LinearLayoutManager(context)
        recylerViewTranding.adapter = RecyclerViewNewsAdapter(arrayList, 3)

        shimmerFrameLayoutTranding.stopShimmer()
        shimmerFrameLayoutTranding.visibility = View.GONE
    }

    private fun showRecyclerListViewCoverStory(arrayList: ArrayList<StoryModel>) {
        recylcerViewCoverStory.setHasFixedSize(true)
        recylcerViewCoverStory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recylcerViewCoverStory.adapter = RecyclerViewStoryAdapter(arrayList)

        shimmerFrameLayoutCoverStory.stopShimmer()
        shimmerFrameLayoutCoverStory.visibility = View.GONE
    }

    private fun showRecyclerListViewNews(arrayList: ArrayList<NewsModel>) {
        recylcerViewNews.setHasFixedSize(true)
        recylcerViewNews.layoutManager = LinearLayoutManager(context)
        recylcerViewNews.adapter = RecyclerViewNewsAdapter(arrayList, arrayList.size)

        shimmerFrameLayoutNews.stopShimmer()
        shimmerFrameLayoutNews.visibility = View.GONE
    }

    fun scrollUp() {
        nestedScrollViewHome.smoothScrollTo(0,0)
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
        shimmerFrameLayoutSlider.startShimmer()
        shimmerFrameLayoutTranding.startShimmer()
        shimmerFrameLayoutCoverStory.startShimmer()
        shimmerFrameLayoutNews.startShimmer()
    }

    override fun onStop() {
        shimmerFrameLayoutSlider.stopShimmer()
        shimmerFrameLayoutTranding.stopShimmer()
        shimmerFrameLayoutCoverStory.stopShimmer()
        shimmerFrameLayoutNews.stopShimmer()
        super.onStop()
    }
}

