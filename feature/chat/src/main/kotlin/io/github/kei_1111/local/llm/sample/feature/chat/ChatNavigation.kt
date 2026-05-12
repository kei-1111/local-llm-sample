package io.github.kei_1111.local.llm.sample.feature.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.chatScreen() {
    composable<ChatRoute> {
        ChatScreenRoute()
    }
}

fun NavController.navigateToChat() {
    navigate(ChatRoute)
}
