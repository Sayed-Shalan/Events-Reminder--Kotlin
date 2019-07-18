package com.sayed.thirdway.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import android.util.Log
import com.github.leonardoxh.livedatacalladapter.Resource

public abstract class NetworkOnlyResource<ResultType, RequestType>  {

    //Dec Data
    private var appExecutors: AppExecutors?=null
    private val result = MediatorLiveData<AppResource<ResultType>>()

    @MainThread
    constructor(appExecutors: AppExecutors){
        this.appExecutors=appExecutors
        result.setValue(AppResource.loading(null))
        fetchFromNetwork()
    }

    //Fetch Call
    private fun fetchFromNetwork(){
        var apiResponse: LiveData<Resource<RequestType>> = createCall()
        result.addSource(apiResponse){response :Resource<RequestType>?->
            run {
                result.removeSource(apiResponse)
                if (response != null && response.isSuccess()) {
                    Log.e("BOUND", "SUCC")
                    appExecutors!!.diskIO().execute {
                        val requestValue: RequestType = processResponse(response)
                        saveCallResult(requestValue)
                        val resultValue: ResultType = transfer(requestValue)
                        appExecutors!!.mainThread().execute { result.setValue(AppResource.success(resultValue)) }
                    }
                } else {
                    Log.e("BOUND", "FAIL")
                    onFetchFailed(response!!)
                    result.setValue(AppResource.error(null, response.error!!.message!!))
                }
            }

        }

    }

    protected fun onFetchFailed(apiResponse: Resource<RequestType>) {}

    fun asLiveData(): LiveData<AppResource<ResultType>> {
        return result
    }

    @WorkerThread
    protected fun processResponse(response: Resource<RequestType>): RequestType {
        return response.resource!!
    }

    @WorkerThread
    protected abstract fun transfer(item: RequestType): ResultType

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun createCall(): LiveData<Resource<RequestType>> //done

    @WorkerThread
    protected fun runLiveData() {
    }
}