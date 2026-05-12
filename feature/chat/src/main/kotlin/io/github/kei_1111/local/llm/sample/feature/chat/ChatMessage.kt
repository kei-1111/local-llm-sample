package io.github.kei_1111.local.llm.sample.feature.chat

data class ChatMessage(
    val role: Role,
    val content: String,
) {
    enum class Role { User, Frieren }
}
