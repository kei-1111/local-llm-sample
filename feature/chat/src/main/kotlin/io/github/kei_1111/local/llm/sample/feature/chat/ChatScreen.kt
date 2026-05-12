package io.github.kei_1111.local.llm.sample.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.kei_1111.local.llm.sample.core.llm.ModelStatus
import io.github.kei_1111.local.llm.sample.core.ui.preview.PreviewScreen
import io.github.kei_1111.local.llm.sample.core.ui.provider.LocalDebounceClicker

@Composable
fun ChatScreen(
    state: ChatUiState,
    actions: ChatActions,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val debounceClicker = LocalDebounceClicker.current
    val currentDismissError by rememberUpdatedState(actions.onDismissError)

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            currentDismissError()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ModelStatusBanner(status = state.modelStatus, onDownload = actions.onDownload)

            MessageList(
                messages = state.messages,
                isGenerating = state.isGenerating,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )

            OutlinedTextField(
                value = state.input,
                onValueChange = actions.onInputChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("メッセージ") },
                placeholder = { Text("フリーレンに話しかける") },
                enabled = !state.isGenerating,
            )

            Button(
                onClick = { debounceClicker.processClick(actions.onSubmit) },
                modifier = Modifier.fillMaxWidth(),
                enabled = canSubmit(state),
            ) {
                Text(if (state.isGenerating) "返事を待ってる..." else "送信")
            }
        }
    }
}

private fun canSubmit(state: ChatUiState): Boolean =
    state.modelStatus is ModelStatus.Available &&
        !state.isGenerating &&
        state.input.isNotBlank()

@Composable
private fun MessageList(
    messages: List<ChatMessage>,
    isGenerating: Boolean,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isGenerating) {
        val target = messages.size + if (isGenerating) 1 else 0
        if (target > 0) listState.animateScrollToItem(target - 1)
    }

    if (messages.isEmpty() && !isGenerating) {
        EmptyHint(modifier = modifier)
        return
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(messages) { msg -> MessageBubble(msg) }
        if (isGenerating) {
            item { GeneratingBubble() }
        }
    }
}

@Composable
private fun EmptyHint(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "フリーレンに話しかけてみてください",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == ChatMessage.Role.User
    val colors = MaterialTheme.colorScheme
    val bubbleColor = if (isUser) colors.primary else colors.surfaceVariant
    val textColor = if (isUser) colors.onPrimary else colors.onSurfaceVariant
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = alignment) {
        Column(
            modifier = Modifier.widthIn(max = 320.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
        ) {
            Text(
                text = if (isUser) "あなた" else "フリーレン",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(bubbleColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                )
            }
        }
    }
}

@Composable
private fun GeneratingBubble() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(2.dp),
                strokeWidth = 2.dp,
            )
        }
    }
}

@Composable
private fun ModelStatusBanner(
    status: ModelStatus,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (status is ModelStatus.Available) return

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            when (status) {
                is ModelStatus.Downloadable -> {
                    Text(
                        text = "モデルのダウンロードが必要です",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Button(onClick = onDownload) {
                        Text("ダウンロード")
                    }
                }
                is ModelStatus.Downloading -> {
                    Text(
                        text = "モデルをダウンロード中...",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                is ModelStatus.Unavailable -> Text(
                    text = "この端末では Gemini Nano を利用できません",
                    style = MaterialTheme.typography.titleMedium,
                )
                is ModelStatus.Available -> Unit
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@PreviewScreen
@Composable
private fun ChatScreenEmptyPreview() {
    ChatScreen(
        state = ChatUiState(modelStatus = ModelStatus.Available),
        actions = ChatActions(),
    )
}

@Suppress("UnusedPrivateMember")
@PreviewScreen
@Composable
private fun ChatScreenWithMessagesPreview() {
    ChatScreen(
        state = ChatUiState(
            modelStatus = ModelStatus.Available,
            messages = listOf(
                ChatMessage(ChatMessage.Role.User, "おはよう"),
                ChatMessage(ChatMessage.Role.Frieren, "……ん。あと10分。"),
                ChatMessage(ChatMessage.Role.User, "今日は何する？"),
                ChatMessage(ChatMessage.Role.Frieren, "別に。新しい魔法でも探そうかな。役に立たないやつ。"),
            ),
        ),
        actions = ChatActions(),
    )
}

@Suppress("UnusedPrivateMember")
@PreviewScreen
@Composable
private fun ChatScreenDownloadablePreview() {
    ChatScreen(
        state = ChatUiState(modelStatus = ModelStatus.Downloadable),
        actions = ChatActions(),
    )
}
