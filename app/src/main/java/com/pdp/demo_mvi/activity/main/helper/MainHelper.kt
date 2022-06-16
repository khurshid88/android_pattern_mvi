package com.pdp.demo_mvi.activity.main.helper

import com.pdp.demo_mvi.model.Post

interface MainHelper {
    suspend fun allPosts(): ArrayList<Post>
    suspend fun deletePost(id: Int): Post
}

