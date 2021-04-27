package com.soufian.todosoufianlabed.userinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soufian.todosoufianlabed.network.TasksRepository
import com.soufian.todosoufianlabed.network.UserInfo
import com.soufian.todosoufianlabed.network.UserInfoRepository
import com.soufian.todosoufianlabed.tasklist.Task
import kotlinx.coroutines.launch

class UserInfoViewModel :  ViewModel() {

    private val repository = UserInfoRepository()
    private val _userInfo = MutableLiveData<UserInfo>()
    public val userInfo: LiveData<UserInfo> = _userInfo

    fun loadinfo() {
        viewModelScope.launch {
            val info = repository.loadInfo()

            val fetchedInfo = info
            _userInfo.value = fetchedInfo!!
        }

    }


    fun editUser(userInfo: UserInfo) {
        viewModelScope.launch {
            val editableInfo = repository.updateInfo(userInfo)!!
            _userInfo.value = editableInfo
        }
    }


}