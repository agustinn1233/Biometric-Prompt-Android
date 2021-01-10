package com.github.agustinf1233.biometricprompt

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            // Validate if the device has/supports biometrics
            checkBiometric()

            val button : Button = findViewById(R.id.showPromptButton)
            button.setOnClickListener { showBiometricPrompt() }
        }

        private fun showBiometricPrompt() {
            val executor = Executors.newSingleThreadExecutor()
            val callback = object : BiometricPrompt.AuthenticationCallback() {

                /**
                 * onAuthenticationError
                 */
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(
                                this@MainActivity,
                                getString(R.string.biometric_prompt_error, errString, errorCode),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }

                /**
                 * onAuthenticationSucceeded
                 */
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(
                                this@MainActivity,
                                getString(R.string.biometric_prompt_succeeded, result.toString()),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }

                /**
                 * onAuthenticationFailed
                 */
                override fun onAuthenticationFailed() {
                    this@MainActivity.runOnUiThread {
                        Toast.makeText(
                                this@MainActivity,
                                getString(R.string.biometric_prompt_failed),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            // Allows user to authenticate using either a Class 3 biometric or
            // their lock screen credential (PIN, pattern, or password).

            // Allows user to authenticate without performing an action, such as pressing a
            // button, after their biometric credential is accepted.

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use account password")
                    .setConfirmationRequired(false)
                    .build()

            BiometricPrompt(this, executor, callback).authenticate(promptInfo)
        }

        private fun checkBiometric() {
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    Log.e("MY_APP_TAG", "No biometric features available on this device.")
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED ->
                    Log.e("MY_APP_TAG", "Biometric security update required.")
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED ->
                    Log.e("MY_APP_TAG", "Biometric error unsupported.")
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN ->
                    Log.e("MY_APP_TAG", "Biometric status unknown.")
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // Prompts the user to create credentials that your app accepts.
                    val enrollIntent = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                        }
                    } else {
                        TODO("VERSION.SDK_INT < R")
                    }
                    val requestCode = 1024
                    startActivityForResult(enrollIntent, requestCode)
                }
            }
        }
    }