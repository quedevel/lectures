import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

fun getApiKey(propertyKey: String): String {
  return gradleLocalProperties(rootDir).getProperty(propertyKey)
}

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("kotlin-parcelize")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
  namespace = "eu.tutorials.happyplaces"
  compileSdk = 33

  defaultConfig {
    applicationId = "eu.tutorials.happyplaces"
    minSdk = 21
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    buildConfigField("String", "GOOGLE_MAPS_API_KEY", getApiKey("GOOGLE_MAPS_API_KEY"))
  }

  buildTypes {
    release {
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
  }
  buildFeatures {
    buildConfig = true
    viewBinding = true
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.9.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  implementation("com.karumi:dexter:6.2.3")
  implementation("de.hdodenhof:circleimageview:3.1.0")
  // Places SDK for Android
  implementation("com.google.android.libraries.places:places:3.1.0")
  implementation("com.google.maps.android:android-maps-utils:2.4.0")
  implementation("com.google.android.gms:play-services-maps:18.1.0")
  debugImplementation("com.infinum.dbinspector:dbinspector:5.4.9")
  releaseImplementation("com.infinum.dbinspector:dbinspector-no-op:5.4.9")

  testImplementation("junit:junit:4.13.2")

  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}