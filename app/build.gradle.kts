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
		versionCode = 5
		versionName = "0.3"

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
		kotlinCompilerExtensionVersion = "1.5.15"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.core.splashscreen)
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.androidx.profileinstaller)
	"baselineProfile"(project(":baselineprofile"))
	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)

	//Baseline Profiles
	implementation(libs.androidx.profileinstaller)

	//Firebase
	implementation(platform(libs.firebase.bom))
	implementation(libs.firebase.crashlytics)
	implementation(libs.firebase.analytics)

	//Coil
	implementation(libs.coil.compose)

	// Dagger - Hilt
	implementation(libs.hilt.android)
	ksp(libs.hilt.compiler)

	// Retrofit
	implementation(libs.retrofit)
	implementation(libs.converter.moshi)

	// Okhttp
	implementation(libs.okhttp)
	implementation(libs.logging.interceptor)

	// Room
	implementation(libs.androidx.room.ktx)
	implementation(libs.androidx.room.runtime)
	ksp(libs.androidx.room.compiler)

	//Preferences Datastore
	implementation (libs.androidx.datastore.preferences)

	//Compose lifecycle
	implementation(libs.androidx.lifecycle.runtime.compose)

	// Navigation
	implementation(libs.androidx.navigation.compose)
	implementation(libs.androidx.hilt.navigation.compose)

	//Apache Poi
	implementation(libs.poi.ooxml)
}