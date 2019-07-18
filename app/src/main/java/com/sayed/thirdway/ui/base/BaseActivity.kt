package com.sayed.thirdway.ui.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.sayed.thirdway.R

import com.sayed.thirdway.utils.AppUtils
import dagger.android.AndroidInjection

abstract class BaseActivity : AppCompatActivity() {

    private val networkChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            noInternetConnection = connectivityManager.activeNetworkInfo == null
            showHideSnackBar()
        }
    }

    private lateinit var loading: Loading
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var snackbar: Snackbar
    private var noInternetConnection: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        loading = Loading(this)

        initSnackBar()

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)


    }

    override fun onResume() {
        super.onResume()
        if (noInternetConnection && !snackbar.isShown) {
            snackbar.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (supportActionBar != null) {
                if (supportActionBar!!.displayOptions and ActionBar.DISPLAY_HOME_AS_UP > 0) {
                    AppUtils.navigateUp(this)
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSnackBar() {
        val white = ContextCompat.getColor(this, R.color.white)

        var rootLayout: ViewGroup? = findViewById(android.R.id.content)
        if (rootLayout == null) rootLayout = window.decorView.findViewById(android.R.id.content)

        if (rootLayout != null) {
            snackbar = Snackbar.make(
                rootLayout,
                resources.getString(R.string.no_internet_connection),
                Snackbar.LENGTH_INDEFINITE
            )
                .setActionTextColor(white)
                .setAction("x") { v -> snackbar.dismiss() }


        }
    }

    private fun showHideSnackBar() {
        if (noInternetConnection)
            snackbar.show()
        else
            snackbar.dismiss()
    }

    fun showLoading(show: Boolean) {
        loading.show(show)
    }

    fun setLoadingMsg(msg: String) {
        loading.setLoadingMsg(msg)
    }



    protected fun initToolbar(toolbar: Toolbar?) {

        if (toolbar != null) setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (toolbar != null) supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    protected fun initToolbarWithNoBack(toolbar: Toolbar?) {
        if (toolbar != null) setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        if (toolbar != null) supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    fun showSnackMsg(msg: String) {
        var rootLayout: ViewGroup? = findViewById(android.R.id.content)
        if (rootLayout == null) rootLayout = window.decorView.findViewById(android.R.id.content)
        if (rootLayout != null) {
            Snackbar.make(rootLayout, msg, Snackbar.LENGTH_SHORT)
                .setActionTextColor(resources.getColor(R.color.white))
                .setAction("Action", null).show()

        }
    }

    fun startNewActivityWithAnim(intent: Intent) {
        startActivity(intent)
    }

    fun startNewActivityWithAnimAndFinish(intent: Intent) {
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        finish()
    }

    //show Toast
    fun showToast(activity: Activity, msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

}
