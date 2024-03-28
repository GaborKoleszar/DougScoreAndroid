// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	id("com.android.application") version "8.3.1" apply false
	id("org.jetbrains.kotlin.android") version "1.9.23" apply false
	id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false
	id("com.google.gms.google-services") version "4.4.1" apply false
	id("com.google.firebase.crashlytics") version "2.9.9" apply false
	id("com.android.test") version "8.3.1" apply false
	id("androidx.baselineprofile") version "1.2.3" apply false
}

buildscript {
	dependencies {
		classpath("com.google.dagger:hilt-android-gradle-plugin:2.51")
	}
}