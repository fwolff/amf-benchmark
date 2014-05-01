package org.granite.benchmark.amf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public abstract class AbstractAMFBenchmark {
	
	private static final long WARMUP_TIME_MS = 20000L;
	
	public AbstractAMFBenchmark() {
	}

	protected abstract String getPlatformName();
	protected abstract void setup() throws Exception;
	protected abstract void serialize(Object o, OutputStream out) throws IOException;
	protected abstract Object deserialize(InputStream in) throws IOException, ClassNotFoundException;
	
	protected void runBenchmak(String[] args) throws Exception {
		if (args.length != 4)
			throw new IllegalArgumentException("Illegal arguments count: " + Arrays.toString(args));
		
		File javaFile = new File(args[0]);
		int count = Integer.parseInt(args[1]);
		File amfFile = new File(args[2]);
		File csvFile = new File(args[3]);

		System.out.println("Reading Java data from file: " + javaFile);
		Object o = readData(javaFile);

		System.out.println("Setting up " + getPlatformName());
		setup();

		System.out.println("Warming up (~" + (WARMUP_TIME_MS / 1000) + " s)...");
		warmup(o, WARMUP_TIME_MS);
		System.gc();
		
		System.out.println("Benchmarking serialization...");
		final long serializationTime = benchmarkSerialization(o, count, (int)javaFile.length());
		System.gc();
		
		byte[] data = getAmfData(o, (int)javaFile.length());
		final int amfDataSize = data.length;
		System.out.println("Saving AMF3 data to file: " + amfFile);
		writeDataToFile(data, amfFile);
		o = null;
		System.gc();
		
		System.out.println("Benchmarking deserialization...");
		final long deserializationTime = benchmarkDeserialization(data, count);
		data = null;
		System.gc();
		
		System.out.println("Writing benchmark result to file: " + csvFile);
		boolean writeHeader = !csvFile.exists();
		PrintWriter writer = new PrintWriter(new FileWriter(csvFile, true));
		if (writeHeader)
			writer.println("Date,Amf3 Size,Iterations,Serialization Time,Deserialization Time");
		writer.println(
			new Date().toString() + "," +
			amfDataSize + "," +
			count + "," +
			String.format(Locale.US, "%,.2f", serializationTime / 1000.0) + "," +
			String.format(Locale.US, "%,.2f", deserializationTime / 1000.0)
		);
		writer.close();
		
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("  Platform                   : " + getPlatformName());
		System.out.println("  Java input file            : " + javaFile);
		System.out.println("  AMF3 output size           : " + size(amfDataSize));
		System.out.println("  Iterations                 : " + count(count));
		System.out.println("  Total serialization time   : " + time(serializationTime));
		System.out.println("  Total deserialization time : " + time(deserializationTime));
		System.out.println("-------------------------------------------------------------------------------");
	}
	
	private static String size(int size) {
		if (size < (1 << 10))
			return String.format(Locale.US, "%,d bytes", size);
		if (size < (1 << 20))
			return String.format(Locale.US, "%,d bytes (%,.1f kb)", size, ((double)size / (1 << 10)));
		return String.format(Locale.US, "%,d bytes (%,.1f mb)", size, ((double)size / (1 << 20)));
	}
	
	private static String time(long milliseconds) {
		return String.format(Locale.US, "%,d ms (%,.2f s)", milliseconds, milliseconds / 1000.0);
	}
	
	private static String count(int count) {
		return String.format(Locale.US, "%,d times", count);
	}
	
	private void warmup(Object o, long timeMs) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		final long t1 = System.currentTimeMillis();
		do {
			serialize(o, baos);
			deserialize(new ByteArrayInputStream(baos.toByteArray()));
			baos.reset();
		}
		while (System.currentTimeMillis() - t1 < timeMs);
	}
	
	private byte[] getAmfData(Object o, int size) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
		serialize(o, baos);
		return baos.toByteArray();
	}

	private long benchmarkSerialization(Object o, int count, int size) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(size);

		final long t1 = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			serialize(o, baos);
			baos.reset();
		}
		final long t2 = System.currentTimeMillis();
		
		return t2 - t1;
	}

	private long benchmarkDeserialization(byte[] data, int count) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		
		final long t1 = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			deserialize(bais);
			bais.reset();
		}
		final long t2 = System.currentTimeMillis();
		
		return t2 - t1;
	}
	
	private static Object readData(File file) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
		Object o = in.readObject();
		in.close();
		return o;
	}
	
	private static void writeDataToFile(byte[] data, File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.close();
	}
}
