package com.riteshapps.zomatoclone.data.repository

import com.riteshapps.zomatoclone.data.local.dao.UserDao
import com.riteshapps.zomatoclone.data.local.datastore.AuthDataStore
import com.riteshapps.zomatoclone.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val authDataStore: AuthDataStore
) {
    val isLoggedIn: Flow<Boolean> = authDataStore.isLoggedIn
    val currentUserId: Flow<Long?> = authDataStore.userId
    val currentUserName: Flow<String> = authDataStore.userName
    val currentUserEmail: Flow<String> = authDataStore.userEmail
    val isAdmin: Flow<Boolean> = authDataStore.isAdmin

    suspend fun login(email: String, password: String): Result<UserEntity> {
        return try {
            val passwordHash = hashPassword(password)
            val user = userDao.login(email, passwordHash)
            if (user != null) {
                authDataStore.saveUserSession(user.id, user.name, user.email, user.isAdmin)
                Result.success(user)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, password: String, phone: String = ""): Result<UserEntity> {
        return try {
            if (userDao.isEmailExists(email)) {
                return Result.failure(Exception("Email already exists"))
            }
            
            val passwordHash = hashPassword(password)
            val user = UserEntity(
                name = name,
                email = email,
                passwordHash = passwordHash,
                phone = phone
            )
            val userId = userDao.insertUser(user)
            val insertedUser = user.copy(id = userId)
            authDataStore.saveUserSession(userId, name, email, false)
            Result.success(insertedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        authDataStore.clearSession()
    }

    fun getCurrentUser(userId: Long): Flow<UserEntity?> {
        return userDao.getUserById(userId)
    }

    suspend fun updateUserProfile(user: UserEntity) {
        userDao.updateUser(user)
        authDataStore.updateUserName(user.name)
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun isEmailExists(email: String): Boolean {
        return userDao.isEmailExists(email)
    }
}
