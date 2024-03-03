plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.16.0"
}

group = "com.balsdon"
version = System.getenv("VERSION_NUMBER")

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    /**
     * intellijVersion must be from the build list
     * @see [https://plugins.jetbrains.com/docs/intellij/android-studio-releases-list.html#2023]
     *
     * requires plugins:
     *    - "android" for access to ADB functions
     *
     * WIP: Jewel
     * @see [https://packages.jetbrains.team/maven/p/kpm/public/org/jetbrains/jewel/jewel-ide-laf-bridge-platform-specific/]
     */

    val intellijVersion = "232.10227.8.2321.11379558" // https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#configuration
    version.set(intellijVersion)
    type.set("AI") // Target Android Studio IDE Platform
    plugins.set(listOf("android"))
}

dependencies {
    // ----- Production dependencies -----
    val rxJava = "3.1.8"
    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:$rxJava")

    // WIP: Jewel
    // The platform version is a supported major IJP version (e.g., 232 or 233 for 2023.2 and 2023.3 respectively)
    // implementation("org.jetbrains.jewel:jewel-ide-laf-bridge-platform-specific:$jewelVersion-ij-${platformVersion}")

    // ----- Testing -----
    val googleTruth = "1.1.4"
    // Google truth
    testImplementation("com.google.truth:truth:$googleTruth")
}

tasks {
    // Set the JVM compatibility versions
    val jvm = "17"

    buildSearchableOptions {
        enabled = false
    }

    compileKotlin {
        kotlinOptions.jvmTarget = jvm
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = jvm
    }

    withType<JavaCompile> {
        sourceCompatibility = jvm
        targetCompatibility = jvm
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = jvm
    }

    patchPluginXml {
        sinceBuild.set("222.*")
        untilBuild.set("233.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
