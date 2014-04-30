package org.granite.benchmark.amf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	protected String getName() {
		return "GraniteDS";
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
	protected void serialize(Object o, OutputStream out) throws IOException {
		SimpleGraniteContext.createThreadInstance(graniteConfig, servicesConfig, null);
		AMF3Serializer serializer = new AMF3Serializer(out);
		serializer.writeObject(o);
		serializer.close();
		GraniteContext.release();
	}

	@Override
	protected Object deserialize(InputStream in) throws IOException, ClassNotFoundException {
		Object o;
		
		SimpleGraniteContext.createThreadInstance(graniteConfig, servicesConfig, null);
		AMF3Deserializer deserializer = new AMF3Deserializer(in);
		o = deserializer.readObject();
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
