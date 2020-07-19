plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.1")

    defaultConfig {
        applicationId = "fr.groggy.racecontrol.tv"
        minSdkVersion(26)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-XXLanguage:+InlineClasses")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    val kotlinCoroutinesVersion = "1.3.7"
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:$kotlinCoroutinesVersion")

    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.leanback:leanback:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")

    val hiltVersion = rootProject.extra["hiltVersion"]
    val androidxHiltVersion = "1.0.0-alpha01"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$androidxHiltVersion")
    kapt("androidx.hilt:hilt-compiler:$androidxHiltVersion")

    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.2")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")

    val okHttpVersion = "4.8.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:$okHttpVersion")

    val moshiVersion = "1.9.3"
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")

    implementation("com.auth0.android:jwtdecode:2.0.0")

    val arrowVersion = "0.10.5"
    implementation("io.arrow-kt:arrow-optics:$arrowVersion")
    implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
    kapt("io.arrow-kt:arrow-meta:$arrowVersion")

    val glideVersion = "4.11.0"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    implementation("com.github.bumptech.glide:okhttp3-integration:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")

    val exoplayerVersion = "2.11.7"
    implementation("com.google.android.exoplayer:exoplayer:$exoplayerVersion")
    implementation("com.google.android.exoplayer:extension-okhttp:$exoplayerVersion")
}

kapt {
    correctErrorTypes = true
}
