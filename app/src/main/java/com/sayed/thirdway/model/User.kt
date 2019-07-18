package com.sayed.thirdway.model

import android.os.Parcel
import android.os.Parcelable

class User : Parcelable {
    var id: Int = 0
    var first_name: String? = null
    var last_name: String? = null

    var email: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val user = o as User?

        if (id != user!!.id) return false
        if (if (first_name != null) first_name != user.first_name else user.first_name != null) return false
        if (if (last_name != null) last_name != user.last_name else user.last_name != null) return false
        return if (email != null) email == user.email else user.email == null
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + if (first_name != null) first_name!!.hashCode() else 0
        result = 31 * result + if (last_name != null) last_name!!.hashCode() else 0
        result = 31 * result + if (email != null) email!!.hashCode() else 0
        return result
    }

    constructor(first_name: String, last_name: String) {
        this.first_name = first_name
        this.last_name = last_name
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.id)
        dest.writeString(this.first_name)
        dest.writeString(this.last_name)
        dest.writeString(this.email)
    }

    protected constructor(`in`: Parcel) {
        this.id = `in`.readInt()
        this.first_name = `in`.readString()
        this.last_name = `in`.readString()
        this.email = `in`.readString()
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User {
                return User(source)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}
