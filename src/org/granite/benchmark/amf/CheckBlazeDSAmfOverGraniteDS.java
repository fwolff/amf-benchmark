package org.granite.benchmark.amf;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;

import org.granite.config.GraniteConfig;
import org.granite.config.flex.ServicesConfig;
import org.granite.context.GraniteContext;
import org.granite.context.SimpleGraniteContext;
import org.granite.messaging.amf.io.AMF3Deserializer;

public class CheckBlazeDSAmfOverGraniteDS {

	public static void main(String[] args) throws Exception {
		if (args.length != 1)
			throw new IllegalArgumentException("Illegal arguments count: " + Arrays.toString(args));

		File amfFile = new File(args[0]);
		
		System.out.println("Trying to deserialize BlazeDS AMF with GraniteDS");
		
		GraniteConfig graniteConfig = new GraniteConfig(
			null,
			BenchmarkGraniteDSAmf.class.getClassLoader().getResourceAsStream("granite-config.xml"),
			null,
			null
		);
		ServicesConfig servicesConfig = new ServicesConfig(null, null, false);
		
		System.out.println("Reading AMF data from: " + amfFile + "...");
		
		SimpleGraniteContext.createThreadInstance(graniteConfig, servicesConfig, null);
		AMF3Deserializer deserializer = new AMF3Deserializer(new FileInputStream(amfFile));
		Object o = deserializer.readObject();
		deserializer.close();
		GraniteContext.release();
		
		if (o instanceof Collection)
			System.out.println(o.getClass().getName() + " (" + ((Collection<?>)o).size() + " elements)");
		else
			System.out.println(o);
	}
}
