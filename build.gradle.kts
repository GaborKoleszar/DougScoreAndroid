// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	alias(libs.plugins.android.gradle.plugin) apply false
	alias(libs.plugins.kotlin.android) apply false
	alias(libs.plugins.kotlin.devtools.ksp) apply false
	alias(libs.plugins.gms.services) apply false
	alias(libs.plugins.firebase.crashlytics) apply false
	alias(libs.plugins.android.test) apply false
	alias(libs.plugins.baseline.profile) apply false
}

buildscript {
	dependencies {
		classpath(libs.hilt.android.gradle.plugin)
	}
}