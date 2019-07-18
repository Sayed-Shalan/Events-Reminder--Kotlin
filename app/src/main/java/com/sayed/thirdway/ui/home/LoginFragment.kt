package com.sayed.thirdway.ui.home


import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.sayed.thirdway.R
import com.sayed.thirdway.api.AppResource
import com.sayed.thirdway.databinding.FragmentLoginBinding
import com.sayed.thirdway.di.Injectable
import com.sayed.thirdway.model.User
import com.sayed.thirdway.other.Const
import com.sayed.thirdway.other.OkCancelCallback
import com.sayed.thirdway.ui.base.BaseFragment
import com.sayed.thirdway.ui.base.Loading
import com.sayed.thirdway.utils.AppUtils
import com.sayed.thirdway.utils.SPUtils
import com.sayed.thirdway.view_model.LoginViewModel
import java.util.*
import org.json.JSONObject
import javax.inject.Inject
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import android.view.View.GONE
import android.view.View.VISIBLE


class LoginFragment : BaseFragment(),Injectable, FacebookCallback<LoginResult> {

    //Dec Data
    lateinit var binding: FragmentLoginBinding
    lateinit var callbackManager: CallbackManager
    lateinit var viewModel: LoginViewModel
    private lateinit var loading: Loading
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    @Inject
    lateinit var spUtils:SPUtils
    lateinit var googleSignInClient: GoogleSignInClient

    /**
     * On Activity result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.RC_GOOGLE -> try {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                if (account!=null&&account.idToken!=null){
                    spUtils.idToken=account.idToken //save token in SP
                    Log.e("Google Token ",account.idToken)
                    checkIfLoggedIn() // check if both logged in
                }
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                Log.e("On Catch ", e.printStackTrace().toString())

            }

            else->callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    /**
     * **** On Create View -> Bind layout - ****************************
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false)
        return binding.root;
    }

    /**
     * **** On View Created ****************************
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //init view model
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        initData()//init data

    }

    /**
     * ON START ******************
     */
    override fun onStart() {
        super.onStart()
        checkIfLoggedIn()

    }

    //init Data
    private fun initData() {
        buildGoogleSigning()
        callbackManager = CallbackManager.Factory.create()
        binding.btnFacebookLogin.setPermissions(Arrays.asList("email","user_events")) //we need permission to read email and user_event
        binding.btnFacebookLogin.fragment=this
        binding.btnFacebookLogin.registerCallback(callbackManager,this)
        loading = Loading(this.activity!!)
        viewModel.observeLogin().observe(viewLifecycleOwner, androidx.lifecycle.Observer { updateViews(it) })
    }

    //init google objects for signing
    private fun buildGoogleSigning() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.google_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.activity!!, gso)
        setListenerForGoogle() //Listener
    }

    //set google button click listsner
    fun setListenerForGoogle(){

        binding.btnGoogle.setOnClickListener {
            val signInIntent: Intent=googleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, Const.RC_GOOGLE)
        }
    }

    //Check if user Has session
    private fun checkIfLoggedIn() {
        binding.btnFacebookLogin.visibility=if (AccessToken.getCurrentAccessToken() != null&&!AccessToken.getCurrentAccessToken().isExpired) GONE else VISIBLE
        binding.btnGoogle.visibility=if (GoogleSignIn.getLastSignedInAccount(activity)!=null) GONE else VISIBLE

        if (AccessToken.getCurrentAccessToken() != null && GoogleSignIn.getLastSignedInAccount(activity)!=null){
            if (AccessToken.getCurrentAccessToken().isExpired){
                AccessToken.setCurrentAccessToken(null)
            }else{
                goToNext()
            }
        }
    }

    //update View
    private fun updateViews(resource: AppResource<JSONObject>?) {
        if (resource==null) return

        //Show Loading
        if (resource.isLoading()){
            showLoading(true)
            return
        }

        showLoading(false)

        //Show Error
        if (resource.isError()){
            showSnackMsg(resources.getString(R.string.error_occurred) ,binding.btnFacebookLogin)
            return
        }

        //Handle Data
        if (resource.isSuccessfully()) setSPData(resource.data)

    }

    //set Data
    private fun setSPData(data: JSONObject?) {
        if (data==null) return
        var user=User()
        user.first_name=data.getString(Const.FIRST_NAME)
        user.last_name=data.getString(Const.LAST_NAME)
        user.email=data.getString(Const.EMAIL)
        user.id=data.getInt(Const.ID)

        //open ok cancel dialog
        AppUtils.buildOkCancelDialog(
            this.activity!!, resources.getString(R.string.continue_as)+" "+(user.first_name)+" "+(user.last_name)+ "?",
            resources.getString(R.string.ok),
            resources.getString(R.string.cancel),
            object : OkCancelCallback {
                override fun onOkClick() {
                    spUtils.user=user
                    checkIfLoggedIn()
                }

                override fun onCancelClick() {
                    AccessToken.setCurrentAccessToken(null)
                }
            })
    }

    //go next fragment
    private fun goToNext() {
        Navigation.findNavController(this.activity!!,R.id.main_nav_host_fragment).navigate(R.id.action_loginFragment_to_eventsFragment)
    }

    //show loading
    fun showLoading(show: Boolean) {
        loading.show(show)
    }

    /**
     * Facebook login Callback methods
     */
    override fun onSuccess(result: LoginResult?) {

        if (result!=null) viewModel.pullLogin(result)
    }

    override fun onCancel() {
    }

    override fun onError(error: FacebookException?) {
    }

}
