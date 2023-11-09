package gabor.koleszar.dougscore.data.remote

import gabor.koleszar.dougscore.common.Constants.POST_URL
import okhttp3.ResponseBody
import retrofit2.http.GET

interface DougScoreApi {

	@GET(POST_URL)
	suspend fun getDougScoreExcelFile(): ResponseBody
}