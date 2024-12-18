package com.prayatna.storyapp.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.prayatna.storyapp.data.remote.response.ListStory
import com.prayatna.storyapp.data.repository.UserRepository
import com.prayatna.storyapp.dummy.DataDummy
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest{

    @Mock
    private lateinit var repository: UserRepository
    private lateinit var viewModel: UserViewModel
    private val dummyStory = DataDummy.generateDummyStoryEntity()

    @Before
    fun setUp() {
        viewModel = UserViewModel(repository)
    }


    @Test
    fun `When get stories should not null and return success`() {
        val expectedStory = MutableLiveData<PagingData<ListStory>>()
        `when`(repository.getStory()).thenReturn(expectedStory)
    }
}