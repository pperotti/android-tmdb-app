import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

// Load local.properties manually
val localProperties =
    Properties().apply {
        val localPropsFile = rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            load(localPropsFile.inputStream())
        }
    }

// Read the property (with fallback if missing)
val apiBaseUrl: String =
    localProperties.getProperty("API_BASE_URL")
        ?: error("API_BASE_URL is not defined in local.properties")

val serviceToken: String =
    localProperties.getProperty("SERVICE_TOKEN")
        ?: error("SERVICE_TOKEN is not defined in local.properties")

android {
    namespace = "com.pperotti.android.moviescatalogapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pperotti.android.moviescatalogapp"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Inject it into BuildConfig
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
        buildConfigField("String", "SERVICE_TOKEN", "\"$serviceToken\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

ktlint {
    android = true
    ignoreFailures = false
    reporters {
        reporter(ReporterType.HTML)
    }
}

detekt {
    toolVersion = "1.23.8"
    config.setFrom(file("../config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

dependencies {

    // Out-Of-The-Box libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.ktx)

    // ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Retrofit
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.converter.gson)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android.testing)
    ksp(libs.hilt.android.compiler)

    // OkHttp
    implementation(libs.okhttp3.okhttp)
    implementation(libs.okhttp3.logging.interceptor)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Annotations
    implementation(libs.androidx.annotation)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Detekt Plugin
    //detektPlugins(libs.detekt.formatting)
}
