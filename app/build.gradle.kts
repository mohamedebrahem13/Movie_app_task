import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)


}

fun getSecret(key: String): String {
    val secretsProps = File(rootDir, "app/secrets.properties")
    if (!secretsProps.exists()) {
        throw GradleException("Missing secrets.properties file at app/secrets.properties")
    }

    val props = Properties().apply {
        load(secretsProps.inputStream())
    }

    return props[key]?.toString()
        ?: throw GradleException("Missing required secret key: $key in secrets.properties")
}


android {
    namespace = "com.movie_app_task"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.movie_app_task"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", "\"${getSecret("API_KEY")}\"")
        buildConfigField("String", "ACCESS_TOKEN", "\"${getSecret("ACCESS_TOKEN")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {
    // Retrofit
    implementation(libs.retrofit)

    // Gson
    implementation(libs.converter.gson)
    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    // Dagger Hilt
    implementation(libs.dagger.hilt.android)

    ksp(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //new navigation compose
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.navigation.compose)
    //room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    annotationProcessor(libs.room.compiler)

    //koin
    implementation(libs.koin.androidx.compose)

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.kt.coil.compose)
    implementation(libs.coil.network.okhttp)

    //serialization
    implementation(libs.kotlinx.serialization.json)

    // Testing
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}