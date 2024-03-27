package gabor.koleszar.dougscore.common

import android.icu.text.SimpleDateFormat
import java.util.Date


object DateTimeFormatter {

	private val defaultFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")

	fun getDefaultFormattedDate(date: Date): String {
		return defaultFormat.format(date)
	}
}