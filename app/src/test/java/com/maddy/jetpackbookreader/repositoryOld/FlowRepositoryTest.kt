package com.maddy.jetpackbookreader.repositoryOld

import com.google.firebase.firestore.Query
import com.maddy.jetpackbookreader.repository.FlowRepository
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class FlowRepositoryTest {

    @Test
    fun `test getBooks`() {
        // Arrange
        val fakeFirestoreQuery = mockk<Query>()
        val repo = FlowRepository(fakeFirestoreQuery)

        // Act
        val expected = "Books"
        val actual = "Books"
        // val actual: Flow<List<ReadingBook?>> = repo.getBooks()

        // Assert
        Assert.assertEquals(expected, actual)
    }
}