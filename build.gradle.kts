object Version {
    const val web3 = "^1.2.9"
    const val bignumberJs = "^9.0.0"
    const val datatablesNetBs4 = "^1.10.21"
    const val ethereumJsWallet = "^0.6.3"
    const val ethereumJsTx = "^1.3.7"
    const val babelPolyFill = "^6.26.0"
    const val jQuery = "^3.3.1"
    const val toastr = "^2.1.4"
    const val qrCode = "^1.4.1"
    const val bootstrap = "^4.5.0"
    const val popperJs = "^1.16.0"
}

buildscript {
    val kotlinVersion = "1.4.32"

    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin", "kotlin-gradle-plugin", kotlinVersion)
    }
}

plugins {
    kotlin("js") version "1.4.32"
}

allprojects {
    group = "io.usoamic"
    version = "1.2.0"
}

repositories {
    mavenCentral()

    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core-js", "1.4.3")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(npm("web3", "^1.6.1"))
    implementation(npm("bignumber.js", "^9.0.1"))
    implementation(npm("datatables.net-bs4", "^1.11.3"))
    implementation(npm("ethereumjs-wallet", "^0.6.5"))
    implementation(npm("ethereumjs-tx", "^2.1.2"))
    implementation(npm("babel-polyfill", "^6.26.0"))
    implementation(npm("jquery", "^3.6.0"))
    implementation(npm("toastr", "^2.1.4"))
    implementation(npm("qrcode", "^1.4.1"))
    implementation(npm("bootstrap", "^4.6.1"))
    implementation(npm("popper.js", "^1.16.0"))

    testImplementation(kotlin("kotlin-test-js"))
}

kotlin.js().browser {}

