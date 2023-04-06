import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
val googleServerClientKey: String = gradleLocalProperties(rootDir).getProperty("GOOGLE_SERVER_CLIENT_KEY")

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization") version Deps.kotlinVersion
}

android {
    namespace = "com.piotrkalin.havira.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.piotrkalin.havira.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 2
        versionName = "0.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //properties.load
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Deps.composeVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            buildConfigField("String", "GOOGLE_SERVER_CLIENT_KEY", googleServerClientKey)
        }
    }
}

dependencies {
    implementation(project(":shared"))

    implementation ("com.google.android.gms:play-services-auth:20.4.1")

    implementation("io.coil-kt:coil-compose:2.3.0")

    //TESTING
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.mockito:mockito-inline:2.13.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.0-Beta")
    testImplementation("app.cash.turbine:turbine:0.12.3")

    implementation(Deps.material3)
    implementation(Deps.material3WindowSize)

    implementation(Deps.kotlinDateTime)

    implementation(Deps.composeUi)
    implementation(Deps.composeUiTooling)
    implementation(Deps.composeUiToolingPreview)
    implementation(Deps.composeFoundation)
    implementation(Deps.activityCompose)
    implementation(Deps.composeIconsExtended)
    implementation(Deps.composeNavigation)

    implementation(Deps.hiltAndroid)

    kapt(Deps.hiltAndroidCompiler)
    kapt(Deps.hiltCompiler)
    implementation(Deps.hiltNavigationCompose)

    implementation(Deps.ktorAndroid)

    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.composeTesting)
    debugImplementation(Deps.composeTestManifest)

    kaptAndroidTest(Deps.hiltAndroidCompiler)
    androidTestImplementation(Deps.hiltTesting)
}