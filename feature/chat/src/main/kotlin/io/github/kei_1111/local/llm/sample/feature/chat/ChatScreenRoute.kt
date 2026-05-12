package io.github.kei_1111.local.llm.sample.feature.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreenRoute(
    viewModel: ChatViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val actions = remember(viewModel) {
        ChatActions(
            onInputChange = viewModel::onInputChange,
            onDownload = viewModel::onDownload,
            onSubmit = viewModel::onSubmit,
            onDismissError = viewModel::onDismissError,
        )
    }
    ChatScreen(state = state, actions = actions)
}
