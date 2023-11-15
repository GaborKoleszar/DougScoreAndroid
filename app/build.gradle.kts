plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("dagger.hilt.android.plugin")
	id("com.google.devtools.ksp")
}

android {
	namespace = "gabor.koleszar.dougscore"
	compileSdk = 34

	defaultConfig {
		applicationId = "gabor.koleszar.dougscore"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.4"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
	implementation("androidx.activity:activity-compose:1.8.0")
	implementation(platform("androidx.compose:compose-bom:2023.10.01"))
	implementation("androidx.core:core-splashscreen:1.0.1")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	//Coil
	implementation("io.coil-kt:coil-compose:2.5.0")

	// Dagger - Hilt
	implementation("com.google.dagger:hilt-android:2.48.1")
	ksp("com.google.dagger:hilt-compiler:2.48.1")

	// Retrofit
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

	// Okhttp
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

	// Room
	val roomVersion = "2.6.0"
	implementation("androidx.room:room-ktx:$roomVersion")
	implementation("androidx.room:room-runtime:$roomVersion")
	ksp("androidx.room:room-compiler:$roomVersion")

	// Navigation
	implementation("androidx.navigation:navigation-compose:2.7.5")

	//Apache Poi
	implementation("org.apache.poi:poi-ooxml:5.2.4")
}