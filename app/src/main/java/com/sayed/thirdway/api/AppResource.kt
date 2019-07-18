package com.sayed.thirdway.api

public class AppResource<T>(var status: Int, var data: T?, var error: String?) {

    //Static Data
    companion object{
        private val STATUS_LOADING = 1
        private val STATUS_SUCCESS = 2
        private val STATUS_ERROR = 3

        public fun <T> success(data: T?): AppResource<T> {
            return AppResource(STATUS_SUCCESS, data, null)
        }

        public fun <T> error(data: T?, error: String): AppResource<T> {
            return AppResource(STATUS_ERROR, data, error)
        }


        public fun <T> loading(data: T?): AppResource<T> {
            return AppResource(STATUS_LOADING, data, null)
        }
    }


    override fun toString(): String {
        return "Resource{" +
                "status=" + status +
                ", error=" + error +
                ", data=" + data +
                '}'.toString()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val resource = o as AppResource<*>?

        if (status != resource!!.status) return false
        if (if (error != null) error != resource.error else resource.error != null) return false
        return if (data != null) data == resource.data else resource.data == null
    }

    override fun hashCode(): Int {
        var result = status
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    public fun isLoading(): Boolean {
        return status == STATUS_LOADING
    }

    public fun isSuccessfully(): Boolean {
        return status == STATUS_SUCCESS
    }

    public fun isError(): Boolean {
        return status == STATUS_ERROR
    }
}