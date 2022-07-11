package com.pdp.demo_mvi.repository

import com.pdp.demo_mvi.activity.main.helper.MainHelper

class PostRepository(private val mainHelper: MainHelper) {

    suspend fun allPosts() = mainHelper.allPosts()
    suspend fun deletePost(id: Int) = mainHelper.deletePost(id)

}

