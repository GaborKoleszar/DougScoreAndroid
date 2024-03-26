package gabor.koleszar.dougscore.common

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
class DougApp : Application(), ImageLoaderFactory {
	override fun newImageLoader(): ImageLoader {

		return ImageLoader.Builder(this)
			.memoryCache {
				MemoryCache.Builder(this)
					.maxSizePercent(0.3)
					.build()
			}
			.diskCache {
				DiskCache.Builder()
					.directory(this.cacheDir.resolve("image_cache"))
					.maxSizePercent(0.03)
					.build()
			}
			.crossfade(true)
			.build()
	}
}