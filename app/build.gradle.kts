plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.ajiraapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ajiraapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)

    // JUnit 4 (required for @Rule and ActivityTestRule)
    testImplementation ("junit:junit:4.13.2")

    // AndroidX Test Ext JUnit (required for ActivityTestRule)
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    testImplementation ("org.robolectric:robolectric:4.7.3")// Check for the latest version

    // Espresso for UI testing
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation ("androidx.test.uiautomator:uiautomator:2.2.0")


    androidTestImplementation ("androidx.test:rules:1.4.0")// Ensure this is included

    testImplementation ("org.mockito:mockito-core:5.7.0")
    testImplementation ("org.mockito:mockito-inline:5.2.0")
    androidTestImplementation ("org.mockito:mockito-android:5.7.0")


    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage")
    implementation ("com.google.firebase:firebase-messaging")
}