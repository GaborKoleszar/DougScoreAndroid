package gabor.koleszar.dougscore.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gabor.koleszar.dougscore.common.Constants
import gabor.koleszar.dougscore.data.local.CarDatabase
import gabor.koleszar.dougscore.data.local.preferences.PreferencesImpl
import gabor.koleszar.dougscore.data.remote.DougScoreApi
import gabor.koleszar.dougscore.domain.preferences.Preferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProviderAppModule {

	@Provides
	@Singleton
	fun provideDougScoreApi(converterFactory: Converter.Factory): DougScoreApi {
		return Retrofit.Builder()
			.baseUrl(Constants.BASE_URL)
			.addConverterFactory(converterFactory)
			.client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.BASIC
			}).build())
			.build()
			.create()
	}

	@Provides
	@Singleton
	fun provideConverterFactory(): Converter.Factory {
		return MoshiConverterFactory.create()
	}

	@Provides
	@Singleton
	fun provideDougScoreDatabase(@ApplicationContext context: Context): CarDatabase {
		return Room.databaseBuilder(
			context,
			CarDatabase::class.java,
			"car_db"
		).build()
	}

	@Provides
	@Singleton
	fun provideSharedPreferences(
		app: Application
	): SharedPreferences {
		return app.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
	}

	@Provides
	@Singleton
	fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
		return PreferencesImpl(sharedPreferences)
	}
}