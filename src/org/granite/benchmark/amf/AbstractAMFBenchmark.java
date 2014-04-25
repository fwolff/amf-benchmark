package org.granite.benchmark.amf;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Locale;

public abstract class AbstractAMFBenchmark {
	
	private ByteArrayOutputStream baos;
	
	public AbstractAMFBenchmark() {
		baos = new ByteArrayOutputStream(1 << 20);
	}

	protected abstract void setup() throws Exception;
	protected abstract byte[] serialize(Object o) throws IOException;
	protected abstract Object deserialize(byte[] data) throws IOException, ClassNotFoundException;
	
	protected void runBenchmak(String[] args) throws Exception {
		if (args.length != 2)
			throw new IllegalArgumentException("Illegal arguments count: " + Arrays.toString(args));
		
		String fileName = args[0];
		int count = Integer.parseInt(args[1]);
		
		System.out.println("Reading data from file: " + fileName);
		Object o = readData(fileName);

		System.out.println("Warming up...");
		setup();
		warmup(o);
		System.gc();
		
		System.out.println("Benchmarking serialization...");
		long serializationTime = benchmarkSerialization(o, count);
		System.gc();
		
		byte[] data = serialize(o);
		baos = null;
		o = null;
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
			return String.format("%,d bytes", size);
		if (size < (1 << 20))
			return String.format("%,d bytes (%,.1f kb)", size, ((double)size / (1 << 10)));
		return String.format("%,d bytes (%,.1f mb)", size, ((double)size / (1 << 20)));
	}
	
	private static String time(long milliseconds) {
		return String.format(Locale.US, "%,d ms (%,.2f s)", milliseconds, milliseconds / 1000.0);
	}
	
	protected ByteArrayOutputStream getOutputStream() {
		baos.reset();
		return baos;
	}
	
	private void warmup(Object o) throws IOException, ClassNotFoundException {
		for (int i = 0; i < 20; i++) {
			byte[] data = serialize(o);
			deserialize(data);
		}
	}

	private long benchmarkSerialization(Object o, int count) throws IOException {
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
			serialize(o);
		long t2 = System.currentTimeMillis();
		
		return t2 - t1;
	}

	private long benchmarkDeserialization(byte[] data, int count) throws IOException, ClassNotFoundException {
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < count; i++)
			deserialize(data);
		long t2 = System.currentTimeMillis();
		
		return t2 - t1;
	}
	
	private static Object readData(String fileName) throws IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		Object o = in.readObject();
		in.close();
		return o;
	}
}
