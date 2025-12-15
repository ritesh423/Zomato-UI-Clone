package com.riteshapps.zomatoclone.domain.repo

import com.riteshapps.zomatoclone.common.ResultState
import com.riteshapps.zomatoclone.data.models.UserData
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun loginWithEmailAndPassword(userData: UserData) : Flow<ResultState<String>>
    fun registerWithEmailAndPassword(userData: UserData) : Flow<ResultState<String>>
}