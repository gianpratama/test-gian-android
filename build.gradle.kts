buildscript {
    repositories {
        google()
        jcenter()
        maven(Maven.jitpack)
    }

    dependencies {
        classpath(ClassPath.androidGradle)
        classpath(ClassPath.kotlinGradle)
        classpath(ClassPath.dexcount)

        classpath(ClassPath.gms)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(Maven.jitpack)
    }
}

tasks.register("clean").configure {
    delete("build")
}
