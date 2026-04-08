package gabor.koleszar.dougscore.common

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

@HiltAndroidApp
class DougApp : Application(), ImageLoaderFactory {
	override fun newImageLoader(): ImageLoader {
		return ImageLoader.Builder(this)
			.memoryCache {
				MemoryCache.Builder(this)
					.maxSizePercent(0.5)
					.build()
			}
			.diskCache {
				DiskCache.Builder()
					.directory(this.cacheDir.resolve("image_cache"))
					.maxSizePercent(0.05)
					.build()
			}
			.okHttpClient {
				OkHttpClient.Builder()
					.addNetworkInterceptor(ImageCacheInterceptor())
					.build()
			}
			.crossfade(true)
			.build()
	}
}

private class ImageCacheInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		return chain.proceed(chain.request()).newBuilder()
			.header("Cache-Control", "public, max-age=604800, immutable")
			.removeHeader("Pragma")
			.build()
	}
}