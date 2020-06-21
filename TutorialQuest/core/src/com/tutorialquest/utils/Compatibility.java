package com.tutorialquest.utils;

import java.util.Random;

public class Compatibility {

	public static PlatformCompatibility platform;

	public abstract static class PlatformCompatibility
	{
		public abstract String format(final String format, final Object... args);


	}

}
