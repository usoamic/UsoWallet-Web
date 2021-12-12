object Version {
    const val web3 = "^1.6.1"
    const val bigNumberJs = "^9.0.1"
    const val datatablesNetBs4 = "^1.11.3"
    const val ethereumJsWallet = "^0.6.5"
    const val ethereumJsTx = "^1.3.7"
    const val babelPolyFill = "^6.26.0"
    const val jQuery = "^3.6.0"
    const val toastr = "^2.1.4"
    const val qrCode = "^1.4.1"
    const val bootstrap = "^4.6.1"
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

    implementation(npm("web3", Version.web3))
    implementation(npm("bignumber.js", Version.bigNumberJs))
    implementation(npm("datatables.net-bs4", Version.datatablesNetBs4))
    implementation(npm("ethereumjs-wallet", Version.ethereumJsWallet))
    implementation(npm("ethereumjs-tx", Version.ethereumJsTx))
    implementation(npm("babel-polyfill", Version.babelPolyFill))
    implementation(npm("jquery", Version.jQuery))
    implementation(npm("toastr", Version.toastr))
    implementation(npm("qrcode", Version.qrCode))
    implementation(npm("bootstrap", Version.bootstrap))
    implementation(npm("popper.js", Version.popperJs))

    testImplementation(kotlin("test-js"))
}

kotlin.js().browser {}

