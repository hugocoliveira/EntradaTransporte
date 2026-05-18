package com.entrada.transporte

import android.app.Application
import com.entrada.transporte.BuildConfig
import com.updater.lib.AppUpdateChecker
import com.updater.lib.UpdateConfig

/**
 * Application class do EntradaTransporte.
 * Inicializa o sistema de atualização automática OTA via GitHub ao subir o processo.
 * Verifica periodicamente se há um novo APK disponível no repositório GitHub
 * e notifica o usuário para instalar.
 */
class ETApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicia o verificador periódico de atualizações OTA via GitHub Releases.
        // Compara o versionCode local com o versionCode em version.json no repositório.
        AppUpdateChecker.init(
            context = this,
            config = UpdateConfig(
                githubOwner = "hugocoliveira",
                githubRepo  = "EntradaTransporte",
                branch      = "master",
                githubToken = BuildConfig.GITHUB_TOKEN.takeIf { it.isNotEmpty() }
            )
        )
    }
}
