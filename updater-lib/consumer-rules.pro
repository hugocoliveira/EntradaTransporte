# Mantém todas as classes públicas do updater-lib intactas para builds com minificação
-keep class com.updater.lib.** { *; }
# Mantém nomes dos BroadcastReceivers para o sistema Android conseguir instanciá-los
-keep public class * extends android.content.BroadcastReceiver
# Mantém nomes dos Workers para o WorkManager conseguir instanciá-los pelo nome da classe
-keep public class * extends androidx.work.Worker
-keep public class * extends androidx.work.ListenableWorker
