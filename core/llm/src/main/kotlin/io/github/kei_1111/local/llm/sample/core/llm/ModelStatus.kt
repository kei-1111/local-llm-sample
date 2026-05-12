package io.github.kei_1111.local.llm.sample.core.llm

sealed interface ModelStatus {
    data object Available : ModelStatus
    data object Downloadable : ModelStatus
    data object Downloading : ModelStatus
    data object Unavailable : ModelStatus
}
