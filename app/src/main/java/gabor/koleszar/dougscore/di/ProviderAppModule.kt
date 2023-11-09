package gabor.koleszar.dougscore.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gabor.koleszar.dougscore.common.Constants
import gabor.koleszar.dougscore.data.local.CarDatabase
import gabor.koleszar.dougscore.data.remote.DougScoreApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProviderAppModule {

	@Provides
	@Singleton
	fun provideDougScoreApi(): DougScoreApi {
		return Retrofit.Builder()
			.baseUrl(Constants.BASE_URL)
			.addConverterFactory(MoshiConverterFactory.create())
			.client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
				level = HttpLoggingInterceptor.Level.BASIC
			}).build())
			.build()
			.create()
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
}