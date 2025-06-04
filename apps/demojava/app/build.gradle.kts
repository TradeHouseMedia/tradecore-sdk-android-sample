plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tradehousemedia.tradecore.java"
    compileSdk = libs.versions.androidCompileSdkVersion.get().toInt()

    defaultConfig {
        applicationId = "com.tradehousemedia.tradecore.java"
        minSdk = libs.versions.androidMinSdkVersion.get().toInt()
        targetSdk = libs.versions.androidTargetSdkVersion.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

//noinspection UseTomlInstead
dependencies {
    // Tradecore
    val tradecoreVersion = "1.3.8"
    implementation("com.tradehousemedia:tradecore-core:$tradecoreVersion")
    implementation("com.tradehousemedia:tradecore-google-ad-manager:$tradecoreVersion")
    implementation("com.tradehousemedia:tradecore-prebid:$tradecoreVersion")
    implementation("com.tradehousemedia:tradecore-amazon:$tradecoreVersion")
    implementation("com.tradehousemedia:tradecore-ima:$tradecoreVersion")

    // Bare IMA
    implementation(libs.androidx.browser)
    implementation(libs.androidx.media)
    implementation(libs.google.ima)

    // Default
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}