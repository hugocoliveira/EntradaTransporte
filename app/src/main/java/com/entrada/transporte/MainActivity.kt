package com.entrada.transporte

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.entrada.transporte.BuildConfig
import com.entrada.transporte.ui.theme.EntradaTransporteTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PACKAGE_APP_CHAMADOR = "com.lit.aplicacaomenuautomatico"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retornarAoAppChamador()
            }
        })

        setContent {
            EntradaTransporteTheme {
                TelaEntradaTransporte(onFechar = { retornarAoAppChamador() })
            }
        }
    }

    private fun retornarAoAppChamador() {
        if (isTaskRoot) {
            val intent = packageManager.getLaunchIntentForPackage(PACKAGE_APP_CHAMADOR)
            if (intent != null) {
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        finishAndRemoveTask()
    }
}

@Composable
fun TelaEntradaTransporte(onFechar: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var codigo by remember { mutableStateOf("") }
    var clearJob by remember { mutableStateOf<Job?>(null) }

    fun processarCodigo(valor: String) {
        val trimmed = valor.trim()
        if (trimmed.isBlank()) return
        Toast.makeText(context, trimmed, Toast.LENGTH_LONG).show()
        clearJob?.cancel()
        clearJob = scope.launch {
            delay(5_000)
            codigo = ""
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "EM p/ Transportes - Lit Solutions",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { novo ->
                        // leitores de código enviam o conteúdo + \n ao pressionar Enter
                        if (novo.contains("\n")) {
                            codigo = novo.replace("\n", "")
                            processarCodigo(codigo)
                        } else {
                            codigo = novo
                        }
                    },
                    label = { Text("Leitura de QR Code / Código de barras") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { processarCodigo(codigo) }),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onFechar) {
                    Text(text = "Fechar")
                }
            }

            Text(
                text = "v${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}
