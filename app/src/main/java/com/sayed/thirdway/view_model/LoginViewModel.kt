package com.sayed.thirdway.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.facebook.login.LoginResult
import com.sayed.thirdway.api.AppResource
import com.sayed.thirdway.repositories.LoginRepository
import com.sayed.thirdway.ui.base.BaseViewModel
import org.json.JSONObject
import javax.inject.Inject

class LoginViewModel//Constructor
@Inject constructor(loginRepository: LoginRepository) : BaseViewModel() {

    //dec data - lifeData
    private val resultLoginLD: LiveData<AppResource<JSONObject>>
    private val pullLoginLD=MutableLiveData<LoginResult>()

    //during create object
    init {
        resultLoginLD=Transformations.switchMap(pullLoginLD,loginRepository::getFacebookUser)
    }

    //pull
    public fun pullLogin(loginResult: LoginResult){
        pullLoginLD.postValue(loginResult)
    }

    //observe
    public fun observeLogin(): LiveData<AppResource<JSONObject>>{
        return resultLoginLD
    }

}