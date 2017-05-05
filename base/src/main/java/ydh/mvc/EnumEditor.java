package ydh.mvc;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

public class EnumEditor extends PropertyEditorSupport {
	Class<? extends Enum<?>> enumClass;
	/**
	 * Parse the Enum from the given text, using the oridinal.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if ( ! StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
			return;
		}
		int value = Integer.parseInt(text);
		setValue(enumClass.getEnumConstants()[value]);
	}

	/**
	 * Format the Enum as String, using the ordinal.
	 */
	@Override
	public String getAsText() {
		Enum<?> value = (Enum<?>) getValue();
		return (value != null ? Integer.toString(value.ordinal()) : "");
	}
}
