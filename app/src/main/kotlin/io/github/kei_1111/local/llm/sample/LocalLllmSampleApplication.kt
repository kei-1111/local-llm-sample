package io.github.kei_1111.local.llm.sample

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class LocalLllmSampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@LocalLllmSampleApplication)
            // 各モジュールで作成したモジュールを追加
            // modules()
        }
    }
}
