package com.pdp.demo_mvi.activity.main.intentstate

sealed class MainIntent {
    object AllPosts : MainIntent()
    object DeletePost : MainIntent()
}


