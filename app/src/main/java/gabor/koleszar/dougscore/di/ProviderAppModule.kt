package gabor.koleszar.dougscore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gabor.koleszar.dougscore.common.Constants
import gabor.koleszar.dougscore.data.local.CarDatabase
import gabor.koleszar.dougscore.data.repository.UserPreferencesRepositoryImpl
import gabor.koleszar.dougscore.data.remote.DougScoreApi
import gabor.koleszar.dougscore.domain.repository.UserPreferencesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton


private const val DB_NAME = "car_db"
private const val DATASTORE_NAME = "settings"

private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

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
			DB_NAME
		).build()
	}

	@Provides
	@Singleton
	fun providePreferencesDataStore(
		@ApplicationContext context: Context
	): DataStore<Preferences> {
		return context.dataStore
	}

	@Provides
	@Singleton
	fun providePreferences(dataStore: DataStore<Preferences>): UserPreferencesRepository {
		return UserPreferencesRepositoryImpl(dataStore)
	}
}