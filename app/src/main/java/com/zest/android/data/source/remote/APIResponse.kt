package com.zest.android.data.source.remote

interface APIResponse<Type> {

    fun onSuccess(result: Type?)

    fun onError(t: Throwable)
}
