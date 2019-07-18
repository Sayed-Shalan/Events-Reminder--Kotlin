package com.sayed.thirdway.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.os.Bundle
import android.util.Log
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginResult
import com.sayed.thirdway.api.AppResource
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class LoginRepository @Inject constructor() {

    //get user data
    public fun getFacebookUser(loginResult: LoginResult) : LiveData<AppResource<JSONObject>>{
        var resultLifeData= MutableLiveData<AppResource<JSONObject>>()
        resultLifeData.postValue(AppResource.loading(null))
        val request: GraphRequest = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { jsonObject: JSONObject, graphResponse: GraphResponse ->
            Log.e("User Data",jsonObject.toString())
            resultLifeData.postValue(AppResource.success(jsonObject))
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,first_name,last_name,email")
        request.setParameters(parameters)
        val executeAsync = request.executeAsync()
        return resultLifeData
    }
}