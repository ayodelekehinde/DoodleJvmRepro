val doodleVersion = "0.9.2"
plugins {
    kotlin("multiplatform") version "1.8.22"
    application
}

group = "io.github.doodlejvm"
version = "1.0"

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val jvmMain by getting{
            val osName = System.getProperty("os.name")
            val targetOs = when {
                osName == "Mac OS X"       -> "macos"
                osName.startsWith("Win"  ) -> "windows"
                osName.startsWith("Linux") -> "linux"
                else                       -> error("Unsupported OS: $osName")
            }

            val targetArch = when (val osArch = System.getProperty("os.arch")) {
                "x86_64", "amd64" -> "x64"
                "aarch64"         -> "arm64"
                else              -> error("Unsupported arch: $osArch")
            }

            val target = "${targetOs}-${targetArch}"
            dependencies {
                implementation("io.nacular.doodle:core:$doodleVersion")
                implementation("io.nacular.doodle:desktop-jvm-$target:$doodleVersion")
                implementation("io.nacular.doodle:controls:$doodleVersion")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

application {
    mainClass.set("MainKt")
}