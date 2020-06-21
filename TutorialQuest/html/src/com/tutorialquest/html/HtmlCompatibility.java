package com.tutorialquest.html;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import com.tutorialquest.utils.Compatibility;

import java.util.Date;


public class HtmlCompatibility extends Compatibility.PlatformCompatibility {


	//https://github.com/schorlet/javabox/blob/master/hellogin/src/main/java/com/hellogin/client/Logger.java
	/**
	 * format: %[width][.precision]conversion<br/>
	 * conversion: s | f | (t | T | tT)<br/><br/>
	 * format("%2s %2s %2s %2s", "a", "b", "c", "d")</br>
	 *  -> " a b c d"<br/><br/>
	 * format("e = %10.4f", Math.E)<br/>
	 *  -> "e = 2,7183"<br/><br/>
	 * format("date = %t", today)<br/>
	 *  -> "date = 12/18/07"<br/><br/>
	 * format("time = %T", today)<br/>
	 *  -> "time = 12:01:26"<br/><br/>
	 * format("datetime = %tT", today)<br/>
	 *  -> "datetime = 12/18/07 12:01:26"<br/><br/>
	 */
	public String format(final String format, final Object... args) {
		final RegExp regex = RegExp.compile("%(\\d+)?(.(\\d+))?([a-z]+)", "gi");
		final StringBuilder sb = new StringBuilder();

		int fromIndex = 0;
		final int length = format.length();

		int argsIndex = 0;

		while (fromIndex < length && argsIndex < args.length) {
			// Find the next match of the highlight regex
			final MatchResult result = regex.exec(format);
			if (result == null) {
				break;
			}

			final int index = result.getIndex();
			final String match = result.getGroup(0);

			final String group1 = result.getGroup(1);
			final Integer width = group1 == null ? null : Integer.valueOf(group1);

			final String group3 = result.getGroup(3);
			final Integer precision = group3 == null ? null : Integer.valueOf(group3);

			final String conversion = result.getGroup(4);

			// Append the characters leading up to the match
			sb.append(format.substring(fromIndex, index));

			// date conversion
			if ("t".equalsIgnoreCase(conversion) || "tt".equalsIgnoreCase(conversion)) {
				final Date arg = (Date) args[argsIndex++];
				final String arg_value;

				if (arg == null) {
					arg_value = "null";

				} else if ("t".equals(conversion)) {
					arg_value = DATE_FORMAT.format(arg);

				} else if ("T".equals(conversion)) {
					arg_value = TIME_FORMAT.format(arg);

				} else {
					arg_value = DATE_TIME_FORMAT.format(arg);
				}

				sb.append(arg_value);
			}
			// decimal conversion
			else if ("f".equals(conversion)) {
				final Double arg = (Double) args[argsIndex++];
				final String arg_value;

				if (arg == null || precision == null) {
					arg_value = String.valueOf(arg);

				} else {
					final String arg_format = "#,##0.".concat(repeat("0", precision - 1)).concat(
						"#");
					arg_value = NumberFormat.getFormat(arg_format).format(arg);
				}

				sb.append(arg_value);
			}
			// string conversion
			else {
				String arg = String.valueOf(args[argsIndex++]);
				if (arg != null && width != null) {
					final int nb_space = width - arg.length();
					arg = arg.concat(repeat(" ", nb_space));
				}
				sb.append(arg);
			}

			// Skip past the matched string
			fromIndex = index + match.length();
			regex.setLastIndex(fromIndex);
		}

		// Append the tail of the string
		if (fromIndex < length) {
			sb.append(format.substring(fromIndex));
		}

		return sb.toString();
	}

	static final DateTimeFormat TIME_FORMAT = DateTimeFormat
		.getFormat(DateTimeFormat.PredefinedFormat.TIME_MEDIUM);

	static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);

	static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat
		.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);

	static String repeat(final String s, final Integer count) {
		if (count == null || count <= 0) return "";

		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(s);
		}
		return sb.toString();
	}

}
