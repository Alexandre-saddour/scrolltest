package com.pierlis.scrolltest

import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import kotlinx.android.synthetic.main.viewpager_recyclerview.*



//
// Recyclerview inside ViewPager inside NestedScrollView
//
class ViewPagerRecyclerView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpager_recyclerview)
        viewpager.apply {
            adapter = CustomPagerAdapter(this@ViewPagerRecyclerView)
            offscreenPageLimit = adapter!!.count
        }
        sliding_tabs.setupWithViewPager(viewpager)

    }

    inner class CustomPagerAdapter(private val mContext: Context) : PagerAdapter() {

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(mContext)
            val layout = inflater.inflate(R.layout.recycler_view_fragment, collection, false) as ViewGroup

            layout.findViewById<RecyclerView>(R.id.recyclerview).apply {
                layoutManager = LinearLayoutManager(layout.context).apply {
                    orientation = LinearLayoutManager.VERTICAL
                }
                adapter = run {
                    com.pierlis.scrolltest.Adapter().apply {
                        submitList((1..20).map { it.toString() }.map { com.pierlis.scrolltest.Adapter.Item(it) })
                    }

                }
            }


            collection.addView(layout)
            return layout
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return 3
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "1"
                1 -> "2"
                2 -> "3"
                else -> null
            }
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            super.setPrimaryItem(container, position, `object`)
            val current = `object` as ViewGroup
            current.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            if (container.layoutParams.height != current.measuredHeight) {
                container.layoutParams.height = current.measuredHeight
                container.requestLayout()
            }
        }

    }


}


