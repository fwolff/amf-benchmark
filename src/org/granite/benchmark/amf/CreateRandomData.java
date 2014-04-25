package org.granite.benchmark.amf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class CreateRandomData {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 3)
			throw new IllegalArgumentException("Illegal arguments count: " + Arrays.toString(args));
		
		Method method = CreateRandomData.class.getDeclaredMethod(args[0], int.class);
		Integer count = Integer.valueOf(args[1]);
		String fileName = args[2];
		
		System.out.println("Invoking " + CreateRandomData.class.getName() + "." + method.getName() + "(" + count + ")");
		Object o = method.invoke(null, count);
		
		System.out.println("Serializing result with " + ObjectOutputStream.class.getName() + " to file: " + fileName);
		serialize(o, fileName);
	}
	
	public static Object createObjectList(int count) {
		Random random = new Random(System.currentTimeMillis());
		
		List<DataObject2> list = new ArrayList<DataObject2>(count);
		for (int i = 0; i < count; i++) {
			int size = random.nextInt(20) + 10;
			
			DataObject2 object2 = new DataObject2();
			object2.setProperty1(random.nextInt());
			object2.setProperty2(randomString(random));
			object2.setProperty3(random.nextBoolean());
			object2.setProperty4(randomString(random));
			object2.setProperty5(random.nextDouble());
			object2.setProperty6(new Date(random.nextLong()));
			object2.setProperty7(randomString(random));
			object2.setProperty8(randomString(random));
			object2.setProperty9(randomString(random));
			object2.setProperty10(randomString(random));
			object2.setObjects(new HashSet<DataObject1>(size));
			
			for (int j = 0; j < size; j++) {
				DataObject1 object1 = new DataObject1();
				object1.setProperty1(random.nextInt());
				object1.setProperty2(randomString(random));
				object1.setProperty3(random.nextBoolean());
				object1.setProperty4(randomString(random));
				object1.setProperty5(random.nextDouble());
				object1.setProperty6(new Date(random.nextLong()));
				object1.setProperty7(randomString(random));
				object1.setProperty8(randomString(random));
				object1.setProperty9(randomString(random));
				object1.setProperty10(randomString(random));
				object1.setObject(object2);
				object2.getObjects().add(object1);
			}
			
			list.add(object2);
		}
		
		return list;
	}

	public static Object createStringList(int count) {
		Random random = new Random(System.currentTimeMillis());
		
		List<String> list = new ArrayList<String>(count);
		for (int i = 0; i < count; i++) {
			String string = randomString(random);
			list.add(string);
		}
		
		return list;
	}
	
	private static void serialize(Object o, String fileName) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(o);
		out.close();
	}
	
	private static String randomString(Random random) {
		int length = random.nextInt(100);
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append((char)random.nextInt(128));
		return sb.toString();
	}
}
