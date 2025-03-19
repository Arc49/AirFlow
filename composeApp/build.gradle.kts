import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    //Serialization
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        //iosX64(),
        iosArm64(),
        //iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            //Ktor Client
            implementation(libs.ktor.client.okhttp)

            //MLkit
            implementation(libs.face.detection)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation("androidx.annotation:annotation:1.7.3")

            //KMP Auth
            implementation(libs.kmpauth.google) //Google One Tap Sign-In
            implementation(libs.kmpauth.uihelper)

            //Coroutines
            implementation(libs.kotlinx.coroutines.core)

            //Cupertino Theme Adaptive
            implementation(libs.cupertino.adaptive)

            //OpenAI
            implementation(libs.openai.client)

            //Ktor
            implementation(libs.ktor.client.core)
            //implementation(libs.ktor.client.negotiation)

            // Json Serialization
            implementation(libs.kotlinx.serialization.json)
            //implementation(libs.ktor.serialization)

            //Voyager Navigation
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.koin)
            implementation(libs.navigator.screen.model)
            implementation(libs.navigator.transitions)
            // Supabase module
            implementation(libs.supabase.postgrest)
            implementation(libs.supabase.realtime)
            //implementation(libs.supabase.compose.auth)
            implementation(libs.supabase.compose.auth.ui)
            implementation(libs.supabase.storage)

            //Koin
            implementation(libs.koin.core)

            //Device Id
            implementation(libs.kdeviceinfo)

            //Camera K Camera:
            implementation(libs.camerak)
            implementation(libs.image.saver.plugin)

            //Permissions
            implementation(libs.permissions.compose)

            //Payment
            implementation(libs.purchases.core)
            implementation(libs.purchases.datetime)   // Optional
            implementation(libs.purchases.either)     // Optional
            implementation(libs.purchases.result)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.arc49.airflow"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.arc49.airflow"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            keyAlias = "upload"
            keyPassword = "airflowkey2025"
            storeFile = file("/Users/mymac/Documents/Github/AirFlow/upload-keystore.jks")
            storePassword = "airflowkey2025"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

