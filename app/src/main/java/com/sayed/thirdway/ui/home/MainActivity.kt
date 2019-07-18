package com.sayed.thirdway.ui.home

import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.Navigation
import com.sayed.thirdway.R
import com.sayed.thirdway.databinding.ActivityMainBinding
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.sayed.thirdway.ui.base.HasSupportFragmentInjectorActivity


class MainActivity : HasSupportFragmentInjectorActivity(), NavController.OnDestinationChangedListener {

    //Dec Data
    lateinit var binding: ActivityMainBinding //Data Binding
    lateinit var navController : NavController

    /**
     **********************************On Create ***************************
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        initData() //init data

    }

    //init data
    private fun initData() {
        navController=Navigation.findNavController(this, R.id.main_nav_host_fragment)
        navController.addOnDestinationChangedListener(this)
    }

    //On Nav host destination changed
    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        binding.appBar.visibility=if(destination.id==R.id.loginFragment) View.GONE else View.VISIBLE //change visibility
    }

}
