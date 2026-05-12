package io.github.kei_1111.local.llm.sample.core.llm

sealed interface PromptResult {
    data class Success(val text: String) : PromptResult
    data class Error(val cause: Throwable) : PromptResult
}
