package io.github.kei_1111.local.llm.sample

import android.app.Application
import io.github.kei_1111.local.llm.sample.core.llm.llmModule
import io.github.kei_1111.local.llm.sample.feature.chat.chatModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class LocalLllmSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LocalLllmSampleApplication)
            modules(llmModule, chatModule)
        }
    }
}
