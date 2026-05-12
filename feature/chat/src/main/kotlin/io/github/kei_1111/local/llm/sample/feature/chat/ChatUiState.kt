package io.github.kei_1111.local.llm.sample.feature.chat

import io.github.kei_1111.local.llm.sample.core.llm.ModelStatus

data class ChatUiState(
    val modelStatus: ModelStatus = ModelStatus.Unavailable,
    val input: String = "",
    val isGenerating: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val errorMessage: String? = null,
)
