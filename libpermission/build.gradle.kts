plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")  
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.enginepermissions.library"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.XDRevil"
                artifactId = "EnginePermissionsAndroid"
                version = "0.2"  
            }
        }
    }
}
