package gabor.koleszar.dougscore.data.remote

import gabor.koleszar.dougscore.domain.model.Car
import gabor.koleszar.dougscore.domain.model.DailyScore
import gabor.koleszar.dougscore.domain.model.WeekendScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class XlsxCarDataParser @Inject constructor() : CarDataParser<Car> {
	override suspend fun parse(stream: InputStream): List<Car> {
		val carIntroductions = mutableListOf<Car>()
		withContext(Dispatchers.IO) {
			try {
				val workBook = XSSFWorkbook(stream)
				val sheet = workBook.getSheetAt(0)
				val rowIterator = sheet.iterator()

				//Skip header rows
				rowIterator.next()
				rowIterator.next()
				rowIterator.next()

				while (rowIterator.hasNext()) {
					val row = rowIterator.next()
					if (row.getCell(YEAR_COLUMN).toString().isEmpty())
						break
					carIntroductions.add(
						Car(
							year = row.getCell(YEAR_COLUMN).numericCellValue.toInt(),
							manufacturer = row.getCell(MANUFACTURER_COLUMN).toString(),
							model = row.getCell(MODEL_COLUMN).toString(),
							weekendScore = WeekendScore(
								styling = row.getCell(W_STYLING_COLUMN).numericCellValue.toInt()
									.toByte(),
								acceleration = row.getCell(W_ACCELERATION_COLUMN).numericCellValue.toInt()
									.toByte(),
								handling = row.getCell(W_HANDLING_COLUMN).numericCellValue.toInt()
									.toByte(),
								funFactor = row.getCell(W_FUN_FACTOR_COLUMN).numericCellValue.toInt()
									.toByte(),
								coolFactor = row.getCell(W_COOL_FACTOR_COLUMN).numericCellValue.toInt()
									.toByte(),
								total = row.getCell(W_TOTAL_COLUMN).numericCellValue.toInt()
									.toByte()
							),
							dailyScore = DailyScore(
								features = row.getCell(D_FEATURE_COLUMN).numericCellValue.toInt()
									.toByte(),
								comfort = row.getCell(D_COMFORT_COLUMN).numericCellValue.toInt()
									.toByte(),
								quality = row.getCell(D_QUALITY_COLUMN).numericCellValue.toInt()
									.toByte(),
								practicality = row.getCell(D_PRACTICALITY_COLUMN).numericCellValue.toInt()
									.toByte(),
								value = row.getCell(D_VALUE_COLUMN).numericCellValue.toInt()
									.toByte(),
								total = row.getCell(D_TOTAL_COLUMN).numericCellValue.toInt()
									.toByte(),
							),
							dougScore = row.getCell(DOUGSCORE_COLUMN).numericCellValue.toInt(),
							videoLink = getYtUrl(row.getCell(VIDEOLINK_COLUMN) as XSSFCell),
							filmingLocationCity = row.getCell(CITY_COLUMN).toString(),
							filmingLocationState = row.getCell(STATE_COLUMN).toString(),
							vehicleCountry = row.getCell(VEHICLE_COUNTRY_COLUMN).toString()
						)
					)
				}
				return@withContext
			} catch (e: IOException) {
				println("---XLSX parsing failed---")
				e.printStackTrace()
				println("---XLSX parsing failed---")
			}
		}
		return carIntroductions
	}

	private fun getYtUrl(url: XSSFCell): String? {
		//Handle different format urls
		//If cell contains 38:57:00.000
		if (url.hyperlink != null)
			return url.hyperlink.address
		//If cell contains =HYPERLINK("https://www.youtube.com/watch?v=EkYVXIWAPnc","39:50")
		else if (url.rawValue != null) {
			val regex = """HYPERLINK\("([^"]+)""".toRegex()
			return try {
				//If there is a timestamp, but no link
				val matchResult = regex.find(url.cellFormula)
				matchResult?.groupValues?.get(1)
			} catch (e: Exception) {
				//Return null if cell contains unknown format
				null
			}
		} else {
			return null
		}
	}

	companion object {
		const val YEAR_COLUMN = 0
		const val MANUFACTURER_COLUMN = 1
		const val MODEL_COLUMN = 2
		const val W_STYLING_COLUMN = 3
		const val W_ACCELERATION_COLUMN = 4
		const val W_HANDLING_COLUMN = 5
		const val W_FUN_FACTOR_COLUMN = 6
		const val W_COOL_FACTOR_COLUMN = 7
		const val W_TOTAL_COLUMN = 8
		const val D_FEATURE_COLUMN = 9
		const val D_COMFORT_COLUMN = 10
		const val D_QUALITY_COLUMN = 11
		const val D_PRACTICALITY_COLUMN = 12
		const val D_VALUE_COLUMN = 13
		const val D_TOTAL_COLUMN = 14
		const val DOUGSCORE_COLUMN = 15
		const val VIDEOLINK_COLUMN = 16
		const val CITY_COLUMN = 17
		const val STATE_COLUMN = 18
		const val VEHICLE_COUNTRY_COLUMN = 19
	}
}