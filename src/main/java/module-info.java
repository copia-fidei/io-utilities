module com.epau.util.io{
	requires java.logging;
	requires org.apache.commons.compress;

	exports com.epau.util.io.directory;
	exports com.epau.util.io.file.tar.gz;
	exports com.epau.util.io.file.unix;
	exports com.epau.util.io;
}