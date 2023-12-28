package gabor.koleszar.dougscore.data.remote

import gabor.koleszar.dougscore.common.Constants.DOUGSCORE_SPREADSHEET_URL
import okhttp3.ResponseBody
import retrofit2.http.GET

interface DougScoreApi {

    @GET(DOUGSCORE_SPREADSHEET_URL)
    suspend fun getDougScoreExcelFile(): ResponseBody
}