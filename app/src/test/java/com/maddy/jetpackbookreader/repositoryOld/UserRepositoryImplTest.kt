package com.maddy.jetpackbookreader.repositoryOld

import com.maddy.jetpackbookreader.data.DataSource
import com.maddy.jetpackbookreader.data.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.util.UUID

// Arrange - Act - Assert

class UserRepositoryImplTest {
    private val dataSource = mockk<DataSource>(relaxed = true)      // we have mocked the DataSource
    private val sut = UserRepositoryImpl(dataSource)

    @Test
    fun `verify correct user parameters are used`() = runTest {
        // Arrange
        val user = buildUser()

        // Act
        sut.saveUser(user)
        val captor = slot<User>()
        coVerify { dataSource.save(capture(captor)) }

        // Assert
        Assert.assertEquals(user.email, captor.captured.email)
    }

    @Test
    fun `verify correct user is retrieved`() = runTest {
        // Arrange
        val email = "superman@dccomics.com"
        coEvery { dataSource.get(any()) } returns buildUser()

        // Act
        val user = sut.getUser(email)

        // Assert
        Assert.assertEquals(email, user.email)
    }

    @Test
    fun `verify user is deleted`() = runTest {
        // Arrange
        val user = buildUser()
        sut.saveUser(user)

        // Act
        val email = "superman@dccomics.com"
        sut.deleteUser(email)

        // Assert
        coVerify { dataSource.clear(any()) }
    }



    companion object {
        fun buildUser() = User(
            id = UUID.randomUUID().toString(),
            email = "superman@dccomics.com",
            fullName = "Clark Kent",
            verificationStatus = User.VerificationStatus.Verified,
            memberShipStatus = User.MemberShipStatus.Free
        )
    }
}

/*
// Same Example with Mockito library


import java.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryImplTestMockito {

    private val dataSource = mock(DataSource::class.java)
    private val sut = UserRepositoryImpl(dataSource)

    @Test
    fun `verify correct user params are used`() = runTest {
        val user = buildUser()

        sut.saveUser(user)

        val captor = argumentCaptor<User>()

        verify(dataSource).save(captor.capture())

        Assert.assertEquals(user.email, captor.firstValue.email)
    }

    @Test
    fun `verify correct user is retrieved`() = runTest {
        val email = "enyasonjnr@gmail.com"

        `when`(dataSource.get(any())).then { buildUser() }

        val user = sut.getUser(email)

        Assert.assertEquals(email, user.email)
    }

    @Test
    fun `verify user is deleted`() = runTest {
        val email = "enyasonjnr@gmail.com"
        sut.deleteUser(email)

        verify(dataSource).clear(any())
    }


    companion object {

        fun buildUser() = User(
            id = UUID.randomUUID().toString(),
            email = "enyasonjnr@gmail.com",
            fullName = "Emmanuel Enya",
            verificationStatus = User.VerificationStatus.Verified,
            memberShipStatus = User.MemberShipStatus.Free
        )
    }
}
*/