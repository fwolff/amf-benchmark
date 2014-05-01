package org.granite.benchmark.amf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;
import flex.messaging.io.amf.Amf3Output;

public class BenchmarkBlazeDSAmf extends AbstractAMFBenchmark {

	private SerializationContext context;
	
	public static void main(String[] args) throws Exception {
		BenchmarkBlazeDSAmf benchmark = new BenchmarkBlazeDSAmf();
		benchmark.runBenchmak(args);
	}

	@Override
	protected String getPlatformName() {
		return "BlazeDS";
	}

	@Override
	protected void setup() throws Exception {
		context = new SerializationContext();
	}

	@Override
	protected void serialize(Object o, OutputStream out) throws IOException {
		Amf3Output serializer = new Amf3Output(context);
		serializer.setOutputStream(out);
		serializer.writeObject(o);
		serializer.close();
	}

	@Override
	protected Object deserialize(InputStream in) throws IOException, ClassNotFoundException {
		Object o;
		
		Amf3Input deserializer = new Amf3Input(context);
		deserializer.setInputStream(in);
		o = deserializer.readObject();
		deserializer.close();
		
		return o;
	}
}
