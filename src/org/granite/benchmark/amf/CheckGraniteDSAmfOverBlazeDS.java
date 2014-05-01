package org.granite.benchmark.amf;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collection;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.Amf3Input;

public class CheckGraniteDSAmfOverBlazeDS {

	public static void main(String[] args) throws Exception {
		if (args.length != 1)
			throw new IllegalArgumentException("Illegal arguments count: " + Arrays.toString(args));

		File amfFile = new File(args[0]);
		
		System.out.println("Trying to deserialize GraniteDS AMF with BlazeDS");
		
		SerializationContext context = new SerializationContext();
		
		System.out.println("Reading AMF data from: " + amfFile + "...");
		Amf3Input deserializer = new Amf3Input(context);
		deserializer.setInputStream(new FileInputStream(amfFile));
		Object o = deserializer.readObject();
		deserializer.close();
		
		if (o instanceof Collection)
			System.out.println(o.getClass().getName() + " (" + ((Collection<?>)o).size() + " elements)");
		else
			System.out.println(o);
	}
}
