/*
 * Copyright (c) 2019 Adyen N.V.
 *
 * This file is open source and available under the MIT license. See the LICENSE file for more info.
 *
 * Created by caiof on 9/4/2019.
 */

package com.adyen.checkout.dropin.service

import android.os.Parcel
import android.os.Parcelable

/**
 * The result from a server call request on the [DropInService]
 */
class CallResult(val type: ResultType, val content: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        ResultType.valueOf(parcel.readString()!!),
        parcel.readString()!!)

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(type.name)
        dest?.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * Indicates the type of the result from the call request.
     */
    enum class ResultType {
        /**
         * Call was successful and payment is finished. Content will be passed along as the result.
         */
        FINISHED,
        /**
         * Call was successful and returned with an [com.adyen.checkout.base.model.payments.Action] that needs to be handled.
         * Content should have the JSON string of the Action object.
         */
        ACTION,
        /**
         * Call failed with an error. Content should have the localized error message.
         */
        ERROR,
        /**
         * Call wants to wait for asynchronous processing. Content is ignored.
         */
        WAIT
    }

    companion object CREATOR : Parcelable.Creator<CallResult> {
        override fun createFromParcel(parcel: Parcel): CallResult {
            return CallResult(parcel)
        }

        override fun newArray(size: Int): Array<CallResult?> {
            return arrayOfNulls(size)
        }
    }
}
