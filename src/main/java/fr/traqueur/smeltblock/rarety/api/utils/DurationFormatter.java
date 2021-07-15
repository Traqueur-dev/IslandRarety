package fr.traqueur.smeltblock.rarety.api.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.time.DurationFormatUtils;

public class DurationFormatter {
	private static long oneMinute = TimeUnit.MINUTES.toMillis(1);
	private static long oneHour = TimeUnit.HOURS.toMillis(1);
	private static long oneDay = TimeUnit.DAYS.toMillis(1);
	public static SimpleDateFormat frenchDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	public static ThreadLocal<DecimalFormat> remainingSeconds = new ThreadLocal<DecimalFormat>() {
		@Override
		protected DecimalFormat initialValue() {
			return new DecimalFormat("0.#");
		}
	};
	public static ThreadLocal<DecimalFormat> remainingSecondsTrailing = new ThreadLocal<DecimalFormat>() {
		@Override
		protected DecimalFormat initialValue() {
			return new DecimalFormat("0.0");
		}
	};

	public static String getRemaining(long millis, boolean milliseconds) {
		return DurationFormatter.getRemaining(millis, milliseconds, true);
	}

	public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
		if (milliseconds && duration < oneMinute) {
			return String.valueOf(
					(trail ? remainingSecondsTrailing : remainingSeconds).get().format((double) duration * 0.001))
					+ 's';
		}
		if (duration >= oneDay) {
			return DurationFormatUtils.formatDuration((long) duration, (String) "dd-HH:mm:ss");
		}
		return DurationFormatUtils.formatDuration((long) duration,
				(String) (String.valueOf(duration >= oneHour ? "HH:" : "") + "mm:ss"));
	}

	public static String getDurationWords(long duration) {
		return DurationFormatUtils.formatDuration((long) duration,
				(String) "d' jours 'H' heures 'm' minutes 's' secondes'");
	}

	public static String getDurationDate(long duration) {
		return frenchDateFormat.format(new Date(duration));
	}

	public static String getCurrentDate() {
		return frenchDateFormat.format(new Date());
	}

}