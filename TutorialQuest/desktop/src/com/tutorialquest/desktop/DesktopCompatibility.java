package com.tutorialquest.desktop;

import com.tutorialquest.utils.Compatibility;

public class DesktopCompatibility extends Compatibility.PlatformCompatibility {

	public String format(final String format, final Object... args) {
		Compatibility.platform = new DesktopCompatibility();
		return String.format(format, args);
	}

}
