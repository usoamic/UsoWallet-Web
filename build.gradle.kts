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
    val kotlinVersion = "1.3.72"

    repositories {
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }

    dependencies {
        classpath("org.jetbrains.kotlin", "kotlin-gradle-plugin", kotlinVersion)
    }
}

plugins {
    val kotlinVersion = "1.3.72"
    id("org.jetbrains.kotlin.js") version kotlinVersion
}

allprojects {
    group = "io.usoamic"
    version = "1.1.2"
}

repositories {
    mavenCentral()
    maven ("https://kotlin.bintray.com/js-externals")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core-js", "1.3.0")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(npm("web3", "^1.2.9"))
    implementation(npm("bignumber.js", "^9.0.0"))
    implementation(npm("datatables.net-bs4", "^1.10.21"))
    implementation(npm("ethereumjs-wallet", "^0.6.3"))
    implementation(npm("ethereumjs-tx", "^1.3.7"))
    implementation(npm("babel-polyfill", "^6.26.0"))
    implementation(npm("jquery", "^3.3.1"))
    implementation(npm("toastr", "^2.1.4"))
    implementation(npm("qrcode", "^1.4.1"))
    implementation(npm("bootstrap", "^4.5.0"))
    implementation(npm("popper.js", "^1.16.0"))

    testImplementation("org.jetbrains.kotlin", "kotlin-test-js")
}

kotlin.target.browser {}

