package io.github.kei_1111.local.llm.sample.core.llm

import kotlinx.coroutines.flow.Flow

interface LocalLlmClient {
    suspend fun checkStatus(): ModelStatus
    fun download(): Flow<ModelStatus>
    suspend fun generate(prompt: String): PromptResult
    fun close()
}
