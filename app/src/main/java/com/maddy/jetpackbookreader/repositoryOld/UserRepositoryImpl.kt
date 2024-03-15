package com.maddy.jetpackbookreader.repositoryOld

import com.maddy.jetpackbookreader.data.DataSource
import com.maddy.jetpackbookreader.data.User

class UserRepositoryImpl constructor(private val dataSource: DataSource) : UserRepository {
    override suspend fun saveUser(user: User) {
        dataSource.save(user)
    }

    override suspend fun getUser(id: String): User {
        return dataSource.get(id) ?: throw IllegalArgumentException("User with id $id not found")
    }

    override suspend fun deleteUser(id: String) {
        dataSource.clear(id)
    }
}