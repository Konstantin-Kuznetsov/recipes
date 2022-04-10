plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Versions.compilesdk)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdkVersion)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            correctErrorTypes = true
        }
    }

    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
        )
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN_VERSION}")

    // Log
    api("com.jakewharton.timber:timber:5.0.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    // DI
    implementation("com.google.dagger:hilt-android:${Versions.DAGGER_VERSION}")
    kapt("com.google.dagger:hilt-android-compiler:${Versions.DAGGER_VERSION}")

    //Room
    implementation("androidx.room:room-runtime:${Versions.ROOM_VERSION}")
    kapt("androidx.room:room-compiler:${Versions.ROOM_VERSION}")
    androidTestImplementation("androidx.room:room-testing:${Versions.ROOM_VERSION}")
    implementation("androidx.room:room-ktx:${Versions.ROOM_VERSION}")

    // Retrofit + OKHttp
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP_VERSION}")
    implementation("com.squareup.retrofit2:retrofit:${Versions.RETROFIT_VERSION}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.RETROFIT_VERSION}")

    // Gson
    implementation("com.google.code.gson:gson:${Versions.GSON_VERSION}")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:${Versions.PAGING_VERSION}")

    // Test
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.8.0")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("android.arch.core:core-testing:1.1.1")
}