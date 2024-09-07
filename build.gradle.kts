// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	id("com.android.application") version "8.6.0" apply false
	id("org.jetbrains.kotlin.android") version "1.9.25" apply false
	id("com.google.devtools.ksp") version "1.9.25-1.0.20" apply false
	id("com.google.gms.google-services") version "4.4.2" apply false
	id("com.google.firebase.crashlytics") version "3.0.2" apply false
	id("com.android.test") version "8.6.0" apply false
	id("androidx.baselineprofile") version "1.3.0" apply false
}

buildscript {
	dependencies {
		classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
	}
}