package com.epau.utilities.io.file.unix;


import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/// Converts octal or decimal numbers into POSIX file permission symbolic notation.
public interface PosixFilePermissionsConverter {

	static Set<PosixFilePermission> permissionsFromDecimal(int decimal) {
		return PosixFilePermissions.fromString(symbolicNotationFromDecimal(decimal));
	}

	static String symbolicNotationFromDecimal(int decimal) {
		return symbolicNotationFromOctal(Integer.toOctalString(decimal));
	}

	static String symbolicNotationFromOctal(String octal) {
		if (octal.isEmpty()) {
			throw new IllegalArgumentException("Octal number must not be empty");
		}
		int len = octal.length();
		if (len > 6) {
			throw new IllegalArgumentException("Octal number must not exceed 6 digits: " + octal);
		}
		// Keep only the last 3 octal digits (owner/group/other permissions).
		// This filters out number like 100644 that include the file type bits.
		if (len > 3) {
			octal = octal.substring(len - 3);
		}
		if (len < 3) {
			for (int i = len; i < 3; i++) {
				// pad with 0s
				//noinspection StringConcatenationInLoop
				octal = "0" + octal;
			}
		}
		var sb = new StringBuilder();
		for (char character : octal.toCharArray()) {
			int num = Character.digit(character, 8);
			if (num < 0) {
				throw new IllegalArgumentException("Invalid octal digit: " + character);
			}
			sb.append((num & 4) == 0 ? '-' : 'r');
			sb.append((num & 2) == 0 ? '-' : 'w');
			sb.append((num & 1) == 0 ? '-' : 'x');
		}
		return sb.toString();
	}
}
