package io.github.kei_1111.local.llm.sample.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kei_1111.local.llm.sample.core.llm.LocalLlmClient
import io.github.kei_1111.local.llm.sample.core.llm.ModelStatus
import io.github.kei_1111.local.llm.sample.core.llm.PromptResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MAX_HISTORY_TURNS = 4

class ChatViewModel(private val llm: LocalLlmClient) : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState())
    val state = _state.asStateFlow()

    init {
        refreshStatus()
    }

    fun refreshStatus() {
        viewModelScope.launch {
            val status = runCatching { llm.checkStatus() }.getOrDefault(ModelStatus.Unavailable)
            _state.update { it.copy(modelStatus = status) }
        }
    }

    fun onInputChange(text: String) {
        _state.update { it.copy(input = text) }
    }

    fun onDownload() {
        viewModelScope.launch {
            runCatching {
                llm.download().collect { status ->
                    _state.update { it.copy(modelStatus = status) }
                }
            }.onFailure { cause ->
                _state.update { it.copy(errorMessage = cause.message ?: "Download failed") }
            }
        }
    }

    fun onSubmit() {
        val prompt = _state.value.input.trim()
        if (prompt.isEmpty() || _state.value.isGenerating) return

        val userMessage = ChatMessage(role = ChatMessage.Role.User, content = prompt)
        val historyForPrompt = _state.value.messages.takeLast(MAX_HISTORY_TURNS * 2)
        val fullPrompt = buildPrompt(historyForPrompt, prompt)

        _state.update {
            it.copy(
                input = "",
                isGenerating = true,
                messages = it.messages + userMessage,
                errorMessage = null,
            )
        }

        viewModelScope.launch {
            when (val result = llm.generate(fullPrompt)) {
                is PromptResult.Success -> {
                    val reply = ChatMessage(
                        role = ChatMessage.Role.Frieren,
                        content = result.text.trim().ifEmpty { "……" },
                    )
                    _state.update {
                        it.copy(isGenerating = false, messages = it.messages + reply)
                    }
                }
                is PromptResult.Error -> _state.update {
                    it.copy(
                        isGenerating = false,
                        errorMessage = result.cause.message ?: "Generation failed",
                    )
                }
            }
        }
    }

    fun onDismissError() {
        _state.update { it.copy(errorMessage = null) }
    }

    override fun onCleared() {
        llm.close()
        super.onCleared()
    }

    private fun buildPrompt(history: List<ChatMessage>, userInput: String): String = buildString {
        appendLine(FRIEREN_PERSONA)
        appendLine()
        history.forEach { msg ->
            val speaker = if (msg.role == ChatMessage.Role.User) "ユーザー" else "フリーレン"
            appendLine("$speaker: ${msg.content}")
        }
        appendLine("ユーザー: $userInput")
        append("フリーレン: ")
    }
}
