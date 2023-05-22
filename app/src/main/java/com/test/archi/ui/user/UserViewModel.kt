package com.test.archi.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.test.archi.ui.base.BaseViewModel
import com.test.archi.repos.RepoRepository
import com.test.archi.repos.UserRepository
import com.test.archi.OpenForTesting
import com.test.archi.utils.AbsentLiveData
import com.test.archi.vo.Repo
import com.test.archi.vo.Resource
import com.test.archi.vo.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@OpenForTesting
class UserViewModel
@Inject constructor(userRepository: UserRepository, repoRepository: RepoRepository) : BaseViewModel()  {
    private val _login = MutableLiveData<String?>()
    val login: LiveData<String?>
        get() = _login
    val repositories: LiveData<Resource<List<Repo>>> = _login.switchMap { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            repoRepository.loadRepos(login)
        }
    }
    val user: LiveData<Resource<User>> = _login.switchMap { login ->
        if (login == null) {
            AbsentLiveData.create()
        } else {
            userRepository.loadUser(login)
        }
    }

    fun setLogin(login: String?) {
        if (_login.value != login) {
            _login.value = login
        }
    }

    fun retry() {
        _login.value?.let {
            _login.value = it
        }
    }
}
