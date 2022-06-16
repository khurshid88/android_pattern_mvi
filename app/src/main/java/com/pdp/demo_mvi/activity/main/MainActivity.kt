package com.pdp.demo_mvi.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_mvc.adapter.PostAdapter
import com.pdp.demo_mvi.R
import com.pdp.demo_mvi.activity.main.intentstate.MainIntent
import com.pdp.demo_mvi.activity.main.intentstate.MainState
import com.pdp.demo_mvi.activity.main.viewmodel.MainViewModel
import com.pdp.demo_mvi.activity.main.viewmodel.MainViewModelFactory
import com.pdp.demo_mvi.model.Post
import com.pdp.demo_mvi.activity.main.helper.MainHelperImpl
import com.pdp.demo_mvi.network.RetrofitBuilder
import kotlinx.coroutines.launch

//https://abhiappmobiledeveloper.medium.com/android-mvi-reactive-architecture-pattern-74e5f1300a87

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        val factory = MainViewModelFactory(MainHelperImpl(RetrofitBuilder.POST_SERVICE))
        viewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        intentAllPosts()
    }

    private fun intentAllPosts() {
        lifecycleScope.launch {
            viewModel.mainIntent.send(MainIntent.AllPosts)
        }
    }

    fun intentDeletePost(id: Int) {
        lifecycleScope.launch {
            viewModel.postId = id
            viewModel.mainIntent.send(MainIntent.DeletePost)
        }
    }

    private fun refreshAdapter(posters: ArrayList<Post>) {
        val adapter = PostAdapter(this, posters)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is MainState.Init -> {
                        Log.d("@@@", "Init")
                    }
                    is MainState.Loading -> {
                        Log.d("@@@", "Loading")
                    }
                    is MainState.AllPosts -> {
                        Log.d("@@@", "PostList")
                        refreshAdapter(it.posts)
                    }
                    is MainState.DeletePost -> {
                        Log.d("@@@", "DeletePost "+it.post)
                        intentAllPosts()
                    }
                    is MainState.Error -> {
                        it
                        Log.d("@@@", "Error " + it)
                    }
                }
            }
        }
    }
}