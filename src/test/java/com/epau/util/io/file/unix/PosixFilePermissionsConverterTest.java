package com.epau.util.io.file.unix;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


class PosixFilePermissionsConverterTest {

	@ParameterizedTest
	@CsvSource(delimiter = '|', textBlock = """
			# permission | decimal | octal
			---------    |       0 |    0000
			--------x    |       1 |    0001
			-------wx    |       3 |    0003
			------r--    |       4 |    0004
			--x-----x    |      65 |    0101
			r--r--r--    |     292 |    0444
			rw-r--r--    |     420 |    0644
			rwx------    |     448 |    0700
			rwxr-xr-x    |     493 |    0755
			rwxrwxrwx    |     511 |    0777
			rw-r--r--    |   33188 | 0100644
			rwxr-xr-x    |   33261 | 0100755
			""")
	void symbolicNotationFromDecimal(String expected, int decimal) {
		assertEquals(expected, PosixFilePermissionsConverter.symbolicNotationFromDecimal(decimal));
	}

	@ParameterizedTest
	@CsvSource(delimiter = '|', textBlock = """
			# permission | octal
			---------    |  000
			--------x    |  001
			-------w-    |  002
			------r--    |  004
			-----x---    |  010
			----w----    |  020
			---r-----    |  040
			---rw----    |  060
			---rwx---    |  070
			--x------    |  100
			-w-------    |  200
			r--------    |  400
			rw-------    |  600
			rwx------    |  700
			r--r--r--    |  444
			r-xr-xr-x    |  555
			rw-r--r--    |  644
			rw-rw-r--    |  664
			rwxr-x---    |  750
			rwxr-xr-x    |  755
			rwxrwx---    |  770
			rwxrwxrwx    |  777
			""")
	void symbolicNotationFromOctal(String expected, String octal) {
		assertEquals(expected, PosixFilePermissionsConverter.symbolicNotationFromOctal(octal));
	}
}