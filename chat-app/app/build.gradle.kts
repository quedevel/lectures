plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("com.google.gms.google-services")
}

android {
  namespace = "com.peachmind.chatapp"
  compileSdk = 33

  defaultConfig {
    applicationId = "com.peachmind.chatapp"
    minSdk = 28
    targetSdk = 33
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
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    viewBinding = true
  }
}

dependencies {

  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.9.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")

  // Import the BoM for the Firebase platform
  implementation(platform("com.google.firebase:firebase-bom:32.2.3"))

  implementation("com.google.firebase:firebase-auth-ktx")
  implementation("com.google.firebase:firebase-database-ktx")
  implementation("com.google.firebase:firebase-messaging-ktx")
  implementation("com.google.firebase:firebase-analytics-ktx")
  // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
  implementation("com.squareup.okhttp3:okhttp:4.11.0")


  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}