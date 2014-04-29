package org.granite.benchmark.amf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Locale;

public abstract class AbstractAMFBenchmark {
	
	public AbstractAMFBenchmark() {
	}

	protected abstract void setup() throws Exception;
	protected abstract void serialize(Object o, OutputStream out) throws IOException;
	protected abstract Object deserialize(InputStream in) throws IOException, ClassNotFoundException;
	
	protected void runBenchmak(String[] args) throws Exception {
		if (args.length != 2)
			throw new IllegalArgumentException("Illegal arguments count: " + Arrays.toString(args));
		
		String fileName = args[0];
		int count = Integer.parseInt(args[1]);
		
		System.out.println("Reading data from file: " + fileName);
		Object o = readData(fileName);

		System.out.println("Warming up...");
		setup();
		byte[] data = warmup(o, 20000L);
		System.gc();
		
		System.out.println("Benchmarking serialization...");
		long serializationTime = benchmarkSerialization(o, count, data.length);
		System.gc();
		
		System.out.println("Benchmarking deserialization...");
		long deserializationTime = benchmarkDeserialization(data, count);
		
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println("  Benchmark class            : " + getClass().getSimpleName());
		System.out.println("  Data file                  : " + fileName);
		System.out.println("  AMF3 serialized size       : " + size(data.length));
		System.out.println("  Iterations                 : " + count);
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
	
	private byte[] warmup(Object o, long time) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		final long t1 = System.currentTimeMillis();
		do {
			serialize(o, baos);
			deserialize(new ByteArrayInputStream(baos.toByteArray()));
			baos.reset();
		}
		while (System.currentTimeMillis() - t1 < time);
		
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
	
	private static Object readData(String fileName) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		Object o = in.readObject();
		in.close();
		return o;
	}
}
