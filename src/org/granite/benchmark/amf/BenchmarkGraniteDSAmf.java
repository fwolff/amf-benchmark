package org.granite.benchmark.amf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.granite.config.GraniteConfig;
import org.granite.config.flex.ServicesConfig;
import org.granite.context.GraniteContext;
import org.granite.context.SimpleGraniteContext;
import org.granite.messaging.amf.io.AMF3Deserializer;
import org.granite.messaging.amf.io.AMF3DeserializerSecurizer;
import org.granite.messaging.amf.io.AMF3Serializer;

public class BenchmarkGraniteDSAmf extends AbstractAMFBenchmark {
	
	private GraniteConfig graniteConfig;
	private ServicesConfig servicesConfig;

	public static void main(String[] args) throws Exception {
		BenchmarkGraniteDSAmf benchmark = new BenchmarkGraniteDSAmf();
		benchmark.runBenchmak(args);
	}

	@Override
	protected void setup() throws Exception {
		graniteConfig = new GraniteConfig(
			null,
			BenchmarkGraniteDSAmf.class.getClassLoader().getResourceAsStream("granite-config.xml"),
			null,
			null
		);
		servicesConfig = new ServicesConfig(null, null, false);
	}

	@Override
	protected byte[] serialize(Object o) throws IOException {
		ByteArrayOutputStream out = getOutputStream();
		
		SimpleGraniteContext.createThreadInstance(graniteConfig, servicesConfig, null);
		
		AMF3Serializer serializer = new AMF3Serializer(out);
		serializer.writeObject(o);
		serializer.close();
		
		GraniteContext.release();
		
		return out.toByteArray();
	}

	@Override
	protected Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		SimpleGraniteContext.createThreadInstance(graniteConfig, servicesConfig, null);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		AMF3Deserializer deserializer = new AMF3Deserializer(bais);
		Object o = deserializer.readObject();
		deserializer.close();
		
		GraniteContext.release();
		
		return o;
	}
	
	public static final class NoopAMF3DeserializerSecurizer implements AMF3DeserializerSecurizer {

		@Override
		public boolean allowInstantiation(String arg0) {
			return true;
		}

		@Override
		public String getParam() {
			return null;
		}

		@Override
		public void setParam(String arg0) {
		}
	}
}
