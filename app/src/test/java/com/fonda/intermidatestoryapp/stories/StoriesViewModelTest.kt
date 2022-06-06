package com.fonda.intermidatestoryapp.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fonda.intermidatestoryapp.model.ListStoryItem
import com.fonda.intermidatestoryapp.model.StoriesResponse
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)

class StoriesViewModelTest{

    @get:Rule
    val testInstantTaskExecutorRule : TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyObserver: Observer<List<ListStoryItem>>

    @Test
    fun `test get list user, return with data`(){
        testCoroutineRule.runBlockingTest {
            val response = mock(StoriesResponse::class.java)

            val viewModel = StoriesViewModel()
            viewModel.getListStory().observeForever(storyObserver)
            viewModel.getListStory("askldjaskldj")

            verify(storyObserver).onChanged(listOf())


        }
    }
}