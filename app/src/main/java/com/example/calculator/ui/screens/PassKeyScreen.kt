package com.example.calculator.ui.screens

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calculator.data.local.PassKeyManager
import com.example.calculator.utils.BiometricUtils
import androidx.fragment.app.FragmentActivity

@Composable
fun PassKeyScreen(
    passKeyManager: PassKeyManager,
    onSuccess: () -> Unit,
    onReset: () -> Unit,
    biometricUtils: BiometricUtils
) {
    var input by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isSettingNew by remember { mutableStateOf(!passKeyManager.isPassKeySet()) }


    val context = LocalContext.current
    val activity = context.findActivity()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(if (isSettingNew) "Set New Pass Key" else "Enter Pass Key")

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text(if (isSettingNew) "New Pass Key" else "Pass Key") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true
        )

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (isSettingNew) {
                if (input.length < 4) {
                    error = "Pass Key must be at least 4 digits"
                    return@Button
                }
                passKeyManager.setPassKey(input)
                onSuccess()
            } else {
                if (passKeyManager.validatePassKey(input)) {
                    onSuccess()
                } else {
                    error = "Incorrect Pass Key"
                }
            }
        }) {
            Text(if (isSettingNew) "Set Pass Key" else "Submit")
        }

        Spacer(Modifier.height(8.dp))

        if (!isSettingNew) {
            Button(onClick = {
                passKeyManager.resetPassKey()
                error = null
                onReset()
                input = ""
                isSettingNew = true
            }) {
                Text("Reset Pass Key")
            }

            Spacer(Modifier.height(8.dp))


            if (activity != null && biometricUtils.isBiometricAvailable(activity)) {
                Button(onClick = {
                    biometricUtils.authenticate(
                        activity = activity,
                        onSuccess = { onSuccess() },
                        onError = { error = it }
                    )
                }) {
                    Text("Use Biometric")
                }
            }
        }
    }
}


fun Context.findActivity(): FragmentActivity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is FragmentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
