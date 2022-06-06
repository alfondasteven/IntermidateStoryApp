package com.fonda.intermidatestoryapp.stories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.fonda.intermidatestoryapp.api.ApiConfig
import com.fonda.intermidatestoryapp.data.StoryPagingSource
import com.fonda.intermidatestoryapp.model.ListStoryItem
import com.fonda.intermidatestoryapp.model.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel : ViewModel() {



    private val _listStory = MutableLiveData<List<ListStoryItem>>()


    fun getListStory(token : String){
        val client = ApiConfig.getApiService().getAllListStories("Bearer $token")
        client.enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if(response.isSuccessful){
                    _listStory.postValue(response.body()?.listStory)
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e("onFailure: " , t.message.toString())
            }
        })
    }

    fun getListStoryMaps(token : String){
        val client = ApiConfig.getApiService().getListStoryMaps("Bearer $token", location = 1)
        client.enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if(response.isSuccessful){
                    _listStory.postValue(response.body()?.listStory)
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e("onFailure: " , t.message.toString())
            }
        })
    }

    fun getListStoryPaging(token : String) : LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(ApiConfig.getApiService(),"Bearer $token")
            }
        ).liveData

    }


    fun getListStory(): LiveData<List<ListStoryItem>>{
        return _listStory
    }
    fun getListStoryMaps(): LiveData<List<ListStoryItem>>{
        return _listStory
    }


}