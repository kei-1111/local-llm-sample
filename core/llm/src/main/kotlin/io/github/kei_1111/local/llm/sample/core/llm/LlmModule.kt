package io.github.kei_1111.local.llm.sample.core.llm

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val llmModule = module {
    singleOf(::MlKitLocalLlmClient) bind LocalLlmClient::class
}
