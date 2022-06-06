package com.fonda.intermidatestoryapp.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fonda.intermidatestoryapp.R
import com.fonda.intermidatestoryapp.adapter.LoadingStateAdapter
import com.fonda.intermidatestoryapp.stories.StoriesAdapter
import com.fonda.intermidatestoryapp.stories.StoriesViewModel
import com.fonda.intermidatestoryapp.databinding.ActivityMainBinding
import com.fonda.intermidatestoryapp.model.ListStoryItem
import com.fonda.intermidatestoryapp.ui.camera.AddStoryActivity
import com.fonda.intermidatestoryapp.ui.detail.DetailUserActivity
import com.fonda.intermidatestoryapp.ui.login.LoginActivity
import com.fonda.intermidatestoryapp.ui.maps.MapsActivity

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val storiesAdapter : StoriesAdapter by lazy {
        StoriesAdapter()
    }
    private lateinit var viewModel: StoriesViewModel

    private lateinit var preferences: SharedPreferences
    private lateinit var name: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(LoginActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val token = preferences.getString(LoginActivity.TOKEN, "").toString()
        name = preferences.getString(LoginActivity.NAME,"Name").toString()

        binding.fabAdd.setOnClickListener { view ->
            if (view.id == R.id.fab_add) {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        storiesAdapter.setOnItemClickCallback(object : StoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                Intent(this@MainActivity,DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_URL ,data.photoUrl)
                    it.putExtra(DetailUserActivity.EXTRA_NAME , data.name)
                    it.putExtra(DetailUserActivity.EXTRA_DESCRIPTION ,data.description)
                    it.putExtra(DetailUserActivity.EXTRA_CREATEDAT , data.createdAt)
                    startActivity(it)
                }
            }

        })

        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(
            StoriesViewModel::class.java)
     //   loadListItem(token)
        binding.apply {
            rvAplikasi.layoutManager = LinearLayoutManager(this@MainActivity)
            rvAplikasi.setHasFixedSize(true)
            rvAplikasi.adapter = storiesAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    storiesAdapter.retry()
                }
            )


            }
        viewModel.getListStoryPaging(token).observe(this){
            if(it != null){
                storiesAdapter.submitData(lifecycle,it)
        //        showLoading(false)
            }
        }
        binding.hiName.text = "Welcome , $name"
    }

    private fun loadListItem(token: String) {
        showLoading(true)
        viewModel.getListStory(token)

    }

    private fun showLoading(state: Boolean) {
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logoutButton ->{
                preferences.edit().apply{
                    clear()
                    apply()
                }
                Intent(this, LoginActivity::class.java).also { startActivity(it) }
                finish()
                return true
            }
            R.id.mapsButton ->{
                Intent(this,MapsActivity::class.java).also { startActivity(it) }
                return true
            }
            else -> return false
        }
    }
}