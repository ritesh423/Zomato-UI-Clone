package com.riteshapps.zomatoclone.domain.useCases

import com.riteshapps.zomatoclone.common.ResultState
import com.riteshapps.zomatoclone.data.models.UserData
import com.riteshapps.zomatoclone.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(val repo: Repo) {

    fun createUser(userData: UserData): Flow<ResultState<String>> {
        return repo.registerWithEmailAndPassword(userData)
    }
}