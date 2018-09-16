package br.com.livroandroid.carros

import android.app.Application
import android.content.Context

import android.util.Log
import androidx.multidex.MultiDexApplication

class CarrosApplication : MultiDexApplication() {
    // Chamado quando o Android criar o processo do aplicativo
    override fun onCreate() {
        super.onCreate()
        // Salva a instância para termos acesso como Singleton
        appInstance = this
    }

    companion object {
        private const val TAG = "CarrosApplication"

        // Singleton da classe Application
        private var appInstance: CarrosApplication? = null

        fun getInstance(): CarrosApplication {
            if (appInstance == null) {
                throw IllegalStateException("Configure a classe de Application no AndroidManifest.xml")
            }
            return appInstance!!
        }

        fun getContext(): Context {
            return getInstance().applicationContext
        }

    }

    // Chamado quando o Android finalizar o processo do aplicativo
    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG, "CarrosApplication.onTerminate()")
    }
}