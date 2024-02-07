import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.io.ByteArrayOutputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    val buildTime = System.currentTimeMillis()
    namespace = "com.flyme.extra.wwrrj.xph"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.flyme.extra.wwrrj.xph"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = getVersionName()
        versionName = "1.0.0." + getVersionName()
        buildConfigField("long", "BUILD_TIME", "$buildTime")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-rules.pro", "proguard-log.pro")
        }
    }
    androidResources {
        additionalParameters += arrayOf("--allow-reserved-package-id", "--package-id", "0x64")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-Xno-param-assertions", "-Xno-call-assertions", "-Xno-receiver-assertions"
        )
    }
    packaging {
        resources {
            excludes += "**"
        }
        dex {
            useLegacyPackaging = true
        }
        applicationVariants.all {
            outputs.all {
                (this as BaseVariantOutputImpl).outputFileName =
                    "FlymeExtra-$versionName($versionCode)-$name-$buildTime.apk"
            }
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    lint { checkReleaseBuilds = false }
}
fun getGitDescribe(): String {
    val out = ByteArrayOutputStream()
    exec {
        commandLine("git", "describe", "--tags", "--always")
        standardOutput = out
    }
    return out.toString().trim()
}

fun getVersionCode(): Int {
    val commitCount = getGitCommitCount()
    val major = 0
    return major + commitCount
}

fun getVersionName(): String {
    return getGitDescribe()
}

fun getGitCommitCount(): Int {
    val out = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = out
    }
    return out.toString().trim().toInt()
}


dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
}
