plugins {
	alias(libs.plugins.android.gradle.plugin)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.hilt.android)
	alias(libs.plugins.kotlin.devtools.ksp)
	alias(libs.plugins.gms.services)
	alias(libs.plugins.firebase.crashlytics)
	alias(libs.plugins.baseline.profile)
	alias(libs.plugins.compose.compiler)
}

android {
	namespace = "gabor.koleszar.dougscore"
	compileSdk = 35

	defaultConfig {
		applicationId = "gabor.koleszar.dougscore"
		minSdk = 26
		targetSdk = 35
		versionCode = 6
		versionName = "0.6"

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
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

composeCompiler {
	enableStrongSkippingMode = true
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