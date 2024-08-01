package gabor.koleszar.dougscore.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import gabor.koleszar.dougscore.presentation.theme.ScoreGreen
import gabor.koleszar.dougscore.presentation.theme.ScoreLightGreen
import gabor.koleszar.dougscore.presentation.theme.ScoreOrange
import gabor.koleszar.dougscore.presentation.theme.ScoreRed
import gabor.koleszar.dougscore.presentation.theme.ScoreYellow

object StyleConstants {
	val DEFAULT_PADDING = 16.dp
	val BORDER_RADIUS = 10.dp
	val SPACER_WIDTH = 8.dp
	val ZERO_ELEVATION = 0.dp
	val ELEVATION = 2.dp

	fun getColorFromSubScore(score: Byte): Color {
		return when (score) {
			in 0..2 -> return ScoreRed
			in 3..4 -> return ScoreOrange
			in 5..6 -> return ScoreYellow
			in 7..8 -> return ScoreLightGreen
			in 9..10 -> return ScoreGreen
			else -> {
				Color.White
			}
		}
	}

	fun getColorFromScore(score: Byte): Color	{
		return when (score) {
			in 0..10 -> return ScoreRed
			in 11..20 -> return ScoreOrange
			in 21..30 -> return ScoreYellow
			in 31..40 -> return ScoreLightGreen
			in 41..50 -> return ScoreGreen
			else -> {
				Color.White
			}
		}
	}
}