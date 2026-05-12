package io.github.kei_1111.local.llm.sample.feature.chat

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatModule = module {
    viewModelOf(::ChatViewModel)
}
