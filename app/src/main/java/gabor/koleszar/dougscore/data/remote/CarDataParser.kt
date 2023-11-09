package gabor.koleszar.dougscore.data.remote

import java.io.InputStream

interface CarDataParser<T> {
	suspend fun parse(stream: InputStream): List<T>
}