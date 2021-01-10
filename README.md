## Documentation
https://developer.android.com/jetpack/androidx/releases/biometric

## Installation
Add this in your app's **build.gradle** file:
```
dependencies {
    // Java language implementation
    implementation "androidx.biometric:biometric:1.0.1"

    // Kotlin
    implementation "androidx.biometric:biometric:1.2.0-alpha01"
  }
```
## Data

Authenticate with biometrics or device credentials, and perform cryptographic operations.

## Version and information.

Version 1.2.0
Version 1.2.0-alpha01
December 2, 2020

androidx.biometric:biometric:1.2.0-alpha01 and androidx.biometric:biometric-ktx:1.2.0-alpha01 are released. Version 1.2.0-alpha01 contains these commits.

## New Features

-Introduced the androidx.biometric:biometric-ktx module, which adds Kotlin-specific APIs and extensions on top of androidx.biometric:biometric.
API Changes

-Added new AuthPrompt APIs for constructing a BiometricPrompt and performing authentication. These APIs do not require the BiometricPrompt to be constructed in an early lifecycle callback, such as onCreate. (I19022)
-Added Kotlin extensions to Fragment and FragmentActivity for the new AuthPrompt APIs. (Iaf98c)