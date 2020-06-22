package com.klab.upright.ui.guide

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.klab.upright.R

class GuideViewPagerAdapter(private val context: Context): PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null

    private val images = arrayOf(
        R.drawable.guide1,
        R.drawable.guide2,
        R.drawable.guide3,
        R.drawable.guide4
    )

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.viewpager_guide, null)
        val image = v.findViewById<ImageView>(R.id.imageView_guide)

        image.setImageResource(images[position])
        val vp = container as ViewPager
        vp.addView(v, 0)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }


}