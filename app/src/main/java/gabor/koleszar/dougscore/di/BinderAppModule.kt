package gabor.koleszar.dougscore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gabor.koleszar.dougscore.data.remote.CarDataParser
import gabor.koleszar.dougscore.data.remote.XlsxCarDataParser
import gabor.koleszar.dougscore.data.repository.CarRepositoryImpl
import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.repository.CarRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class BinderAppModule {

	@Binds
	abstract fun bindCarDataParser(carDataParser: XlsxCarDataParser): CarDataParser<Car>

	@Binds
	abstract fun bindCarIntroductionRepository(impl: CarRepositoryImpl): CarRepository
}