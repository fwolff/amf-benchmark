package org.granite.benchmark.amf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import flex.messaging.io.MessageIOConstants;
import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.AmfMessageDeserializer;
import flex.messaging.io.amf.AmfMessageSerializer;

public class BenchmarkBlazeDSAmf extends AbstractAMFBenchmark {

	public static void main(String[] args) throws Exception {
		BenchmarkBlazeDSAmf benchmark = new BenchmarkBlazeDSAmf();
		benchmark.runBenchmak(args);
	}

	@Override
	protected void setup() throws Exception {
	}

	@Override
	protected byte[] serialize(Object o) throws IOException {
		ByteArrayOutputStream out = getOutputStream();
		
		AmfMessageSerializer serializer = new AmfMessageSerializer();
		serializer.setVersion(MessageIOConstants.AMF3);
		serializer.initialize(new SerializationContext(), out, null);
		serializer.writeObject(o);
		
		return out.toByteArray();
	}

	@Override
	protected Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		
		AmfMessageDeserializer deserializer = new AmfMessageDeserializer();
		deserializer.initialize(new SerializationContext(), bais, null);
		Object o = deserializer.readObject();
		bais.close();
		
		return o;
	}
}
