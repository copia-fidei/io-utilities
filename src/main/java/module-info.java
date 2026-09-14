module com.epau.utilities.io{
	requires java.logging;
	requires org.apache.commons.compress;

	exports com.epau.utilities.io.directory;
	exports com.epau.utilities.io.file.tar.gz;
	exports com.epau.utilities.io.file.unix;
	exports com.epau.utilities.io;

//	requires org.junit.jupiter;
}