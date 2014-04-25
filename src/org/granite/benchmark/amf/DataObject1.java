package org.granite.benchmark.amf;

import java.io.Serializable;
import java.util.Date;

public class DataObject1 implements Serializable {

	private static final long serialVersionUID = 1L;

	private int property1;
	private String property2;
	private boolean property3;
	private String property4;
	private double property5;
	private Date property6;
	private String property7;
	private String property8;
	private String property9;
	private String property10;
	private DataObject2 object;
	
	public int getProperty1() {
		return property1;
	}
	public void setProperty1(int property1) {
		this.property1 = property1;
	}
	public String getProperty2() {
		return property2;
	}
	public void setProperty2(String property2) {
		this.property2 = property2;
	}
	public boolean isProperty3() {
		return property3;
	}
	public void setProperty3(boolean property3) {
		this.property3 = property3;
	}
	public String getProperty4() {
		return property4;
	}
	public void setProperty4(String property4) {
		this.property4 = property4;
	}
	public double getProperty5() {
		return property5;
	}
	public void setProperty5(double property5) {
		this.property5 = property5;
	}
	public Date getProperty6() {
		return property6;
	}
	public void setProperty6(Date property6) {
		this.property6 = property6;
	}
	public String getProperty7() {
		return property7;
	}
	public void setProperty7(String property7) {
		this.property7 = property7;
	}
	public String getProperty8() {
		return property8;
	}
	public void setProperty8(String property8) {
		this.property8 = property8;
	}
	public String getProperty9() {
		return property9;
	}
	public void setProperty9(String property9) {
		this.property9 = property9;
	}
	public String getProperty10() {
		return property10;
	}
	public void setProperty10(String property10) {
		this.property10 = property10;
	}
	public DataObject2 getObject() {
		return object;
	}
	public void setObject(DataObject2 object) {
		this.object = object;
	}
}
