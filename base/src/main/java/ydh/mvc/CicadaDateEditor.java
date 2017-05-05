package ydh.mvc;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

public class CicadaDateEditor extends PropertyEditorSupport {
	private final String strDateFormat;
	private final String strDatetimeFormat;
	private final SimpleDateFormat dateFormat;
	private final SimpleDateFormat datetimeFormat;
	
	public CicadaDateEditor(String dateFormat, String datetimeFormat) {
		this.strDateFormat = dateFormat;
		this.strDatetimeFormat = datetimeFormat;
		this.dateFormat = new SimpleDateFormat(dateFormat);
		this.datetimeFormat = new SimpleDateFormat(datetimeFormat);
	}

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if ( ! StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
			return;
		}
		text = text.trim();
		int textLen = text.length();
		int dateLen = this.strDateFormat.length();
		int datetimeLen = this.strDatetimeFormat.length();
		if (textLen == dateLen) {
			try {
				setValue(this.dateFormat.parse(text));
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		} else if (textLen == datetimeLen) {
			try {
				setValue(this.datetimeFormat.parse(text));
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		} else {
			throw new IllegalArgumentException(
					"Could not parse date: it is not exactly characters long");
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? this.dateFormat.format(value) : "");
	}

}
