plugins {
    id("io.micronaut.minimal.application") version "4.0.0"
    id("io.micronaut.aot") version "4.0.0"
    id("io.micronaut.graalvm") version "4.0.0"
}

version = "0.1"
group = "com.example"

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly(mn.logback.classic)
}

application {
    mainClass.set("com.example.Application")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(20)
        vendor = JvmVendorSpec.ORACLE
    }
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
    aot {
        optimizeServiceLoading.set(true)
        convertYamlToJava.set(true)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}

graalvmNative.toolchainDetection.set(false)

tasks {
    nativeCompile {
        options.get().apply {
            debug.set(true)
            verbose.set(true)
            quickBuild.set(true) // for dev mode only, use GRAALVM_QUICK_BUILD instead
            buildArgs.add("-H:+ReportExceptionStackTraces")
            classpath.from.add(file("./build/libs/demo-0.1.jar"))
        }
    }
}
