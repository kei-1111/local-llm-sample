package io.github.kei_1111.local.llm.sample.core.llm

import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.prompt.Generation
import com.google.mlkit.genai.prompt.GenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

internal class MlKitLocalLlmClient : LocalLlmClient {

    private val client: GenerativeModel = Generation.getClient()

    override suspend fun checkStatus(): ModelStatus = when (client.checkStatus()) {
        FeatureStatus.AVAILABLE -> ModelStatus.Available
        FeatureStatus.DOWNLOADABLE -> ModelStatus.Downloadable
        FeatureStatus.DOWNLOADING -> ModelStatus.Downloading
        FeatureStatus.UNAVAILABLE -> ModelStatus.Unavailable
        else -> ModelStatus.Unavailable
    }

    override fun download(): Flow<ModelStatus> = flow<ModelStatus> {
        emit(ModelStatus.Downloading)
        client.download().collect { /* progress events ignored — UI shows indeterminate */ }
    }.onCompletion { cause -> if (cause == null) emit(ModelStatus.Available) }

    override suspend fun generate(prompt: String): PromptResult = try {
        val text = client.generateContent(prompt).candidates.firstOrNull()?.text.orEmpty()
        PromptResult.Success(text)
    } catch (e: GenAiException) {
        PromptResult.Error(e)
    }

    override fun close() {
        client.close()
    }
}
