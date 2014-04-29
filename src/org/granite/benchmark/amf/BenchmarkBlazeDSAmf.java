package org.granite.benchmark.amf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import flex.messaging.io.MessageIOConstants;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.AmfMessageDeserializer;
import flex.messaging.io.amf.AmfMessageSerializer;

public class BenchmarkBlazeDSAmf extends AbstractAMFBenchmark {

	private SerializationContext serializationContext;
	
	public static void main(String[] args) throws Exception {
		BenchmarkBlazeDSAmf benchmark = new BenchmarkBlazeDSAmf();
		benchmark.runBenchmak(args);
	}

	@Override
	protected void setup() throws Exception {
		serializationContext = new SerializationContext();
	}

	@Override
	protected void serialize(Object o, OutputStream out) throws IOException {
		AmfMessageSerializer serializer = new AmfMessageSerializer();
		serializer.setVersion(MessageIOConstants.AMF3);
		serializer.initialize(serializationContext, out, null);
		serializer.writeObject(o);
	}

	@Override
	protected Object deserialize(InputStream in) throws IOException, ClassNotFoundException {
		Object o;
		
		AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
		deserializer.initialize(serializationContext, in, null);
		o = deserializer.readObject();
		in.close();
		
		return o;
	}
}
