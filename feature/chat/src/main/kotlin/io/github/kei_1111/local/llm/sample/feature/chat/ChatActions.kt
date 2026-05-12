package io.github.kei_1111.local.llm.sample.feature.chat

class ChatActions(
    val onInputChange: (String) -> Unit = {},
    val onDownload: () -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onDismissError: () -> Unit = {},
)
