package com.soufian.todosoufianlabed.network

import android.net.Uri
import android.util.Log
import com.soufian.todosoufianlabed.tasklist.Task
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserInfoRepository {

    private val userWebService = Api.userService

    suspend fun loadInfo(): UserInfo?  {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateInfo(user: UserInfo): UserInfo? {
        val response = userWebService.update(user)
        return if (response.isSuccessful) response.body() else null
    }

}