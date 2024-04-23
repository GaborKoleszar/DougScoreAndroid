plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("dagger.hilt.android.plugin")
	id("com.google.devtools.ksp")
	id("com.google.gms.google-services")
	id("com.google.firebase.crashlytics")
	id("androidx.baselineprofile")
}

android {
	namespace = "gabor.koleszar.dougscore"
	compileSdk = 34

	defaultConfig {
		applicationId = "gabor.koleszar.dougscore"
		minSdk = 26
		targetSdk = 34
		versionCode = 4
		versionName = "0.2"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			isShrinkResources = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.11"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	implementation("androidx.core:core-ktx:1.13.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation("androidx.activity:activity-compose:1.9.0")
	implementation(platform("androidx.compose:compose-bom:2024.04.01"))
	implementation("androidx.core:core-splashscreen:1.0.1")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.profileinstaller:profileinstaller:1.3.1")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	//Baseline Profiles
	"baselineProfile"(project(":baselineprofile"))

	//Firebase
	implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
	implementation("com.google.firebase:firebase-crashlytics")
	implementation("com.google.firebase:firebase-analytics")

	//Coil
	implementation("io.coil-kt:coil-compose:2.6.0")

	// Dagger - Hilt
	implementation("com.google.dagger:hilt-android:2.51")
	ksp("com.google.dagger:hilt-compiler:2.51")

	// Retrofit
	implementation("com.squareup.retrofit2:retrofit:2.11.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.11.0")

	// Okhttp
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

	// Room
	implementation("androidx.room:room-ktx:2.6.1")
	implementation("androidx.room:room-runtime:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")

	//Preferences Datastore
	implementation ("androidx.datastore:datastore-preferences:1.1.0")

	//Compose lifecycle
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

	// Navigation
	implementation("androidx.navigation:navigation-compose:2.7.7")
	implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

	//Apache Poi
	implementation("org.apache.poi:poi-ooxml:5.2.5")
}