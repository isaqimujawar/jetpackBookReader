package com.maddy.jetpackbookreader.repositoryOld

import com.maddy.jetpackbookreader.data.User

interface UserRepository {
    suspend fun saveUser(user: User)

    suspend fun getUser(id: String): User

    suspend fun deleteUser(id: String)
}