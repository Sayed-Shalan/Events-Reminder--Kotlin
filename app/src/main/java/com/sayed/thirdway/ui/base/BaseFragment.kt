package com.sayed.thirdway.ui.base


import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.View
import com.sayed.thirdway.R
import com.sayed.thirdway.di.Injectable
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment() {

    //data

    override fun onCreate(savedInstanceState: Bundle?) {
        if (this is Injectable) {
            AndroidSupportInjection.inject(this)
        }

        super.onCreate(savedInstanceState)
    }


    fun showSnackMsg(msg: String, view: View?) {
        if (view != null) {
            Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .setActionTextColor(resources.getColor(R.color.white))
                .setAction("Action", null).show()

        }
    }

    fun setSwipeRefreshColors(refreshLayout: SwipeRefreshLayout) {
        val c1 = resources.getColor(R.color.thirdway_theme)
        val c2 = resources.getColor(R.color.dark_gray_alpha)
        val c3 = resources.getColor(R.color.black)

        refreshLayout.setColorSchemeColors(c1, c2, c3)
    }

}
