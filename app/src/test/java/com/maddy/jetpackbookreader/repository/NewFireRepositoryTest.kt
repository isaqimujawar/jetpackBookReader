package com.maddy.jetpackbookreader.repository

class NewFireRepositoryTest {
   /* @MockK
    private lateinit var mockFirestoreQuery: Query
    private lateinit var repository: NewFireRepository

    @Before
    fun setup() {
        mockFirestoreQuery = mockk()
        repository = NewFireRepository(mockFirestoreQuery)
    }

    @Test
    fun `test getAllBooks success`() {
        val bookList = getDummyBookList()

        coEvery {
            mockFirestoreQuery.get().await().documents.map { it.toObject(ReadingBook::class.java) }
        } returns bookList

        runTest {
            repository.getAllBooks()
            val result = repository.bookListStateFlow.value
            assertEquals(bookList, result)
        }
    }*/

    /*import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MyRepositoryTest {

    private lateinit var repository: MyRepository
    private lateinit var mockApiService: ApiService

    @Before
    fun setup() {
        mockApiService = mockk()
        repository = MyRepository(mockApiService)
    }

    @Test
    fun `test fetchUsers success`() = runBlockingTest {
        // Given
        val userList = listOf(
            User(id = 1, name = "User 1"),
            User(id = 2, name = "User 2")
        )
        coEvery { mockApiService.getUsers() } returns userList

        // When
        repository.fetchUsers()

        // Then
        val result = repository.userListStateFlow.first()
        assertEquals(userList, result)
        assertNull(repository.errorStateFlow.first())
    }

    @Test
    fun `test fetchUsers error`() = runBlockingTest {
        // Given
        val errorMessage = "Failed to fetch users"
        coEvery { mockApiService.getUsers() } throws Exception(errorMessage)

        // When
        repository.fetchUsers()

        // Then
        val result = repository.userListStateFlow.first()
        assertEquals(emptyList<User>(), result)
        assertEquals(errorMessage, repository.errorStateFlow.first())
    }
}
*/

}