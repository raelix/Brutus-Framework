package com.brutus.driver.hwsw.shared;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class Response {
	private int led0;
	private int led1;
	private int led2;
	private int led3;
	private int led4;
	private int led5;
	private int led6;
	private int led7;
	private int pot0;
	private int pot1;
	private int pot2;
	private int pot3;
	private String btn4;
	private String btn5;
	private String btn6;
	private String btn7;
	
	@XmlElement
	public int getLed0() {
		return led0;
	}
	public void setLed0(int led0) {
		this.led0 = led0;
	}
	@XmlElement
	public int getLed1() {
		return led1;
	}
	public void setLed1(int led1) {
		this.led1 = led1;
	}
	@XmlElement
	public int getLed2() {
		return led2;
	}
	public void setLed2(int led2) {
		this.led2 = led2;
	}
	@XmlElement
	public int getLed3() {
		return led3;
	}
	public void setLed3(int led3) {
		this.led3 = led3;
	}
	@XmlElement
	public int getLed4() {
		return led4;
	}
	public void setLed4(int led4) {
		this.led4 = led4;
	}
	@XmlElement
	public int getLed5() {
		return led5;
	}
	public void setLed5(int led5) {
		this.led5 = led5;
	}
	@XmlElement
	public int getLed6() {
		return led6;
	}
	public void setLed6(int led6) {
		this.led6 = led6;
	}
	@XmlElement
	public int getLed7() {
		return led7;
	}
	public void setLed7(int led7) {
		this.led7 = led7;
	}
	@XmlElement
	public int getPot0() {
		return pot0;
	}
	public void setPot0(int pot0) {
		this.pot0 = pot0;
	}
	@XmlElement
	public int getPot1() {
		return pot1;
	}
	public void setPot1(int pot1) {
		this.pot1 = pot1;
	}
	@XmlElement
	public int getPot2() {
		return pot2;
	}
	public void setPot2(int pot2) {
		this.pot2 = pot2;
	}
	@XmlElement
	public int getPot3() {
		return pot3;
	}
	public void setPot3(int pot3) {
		this.pot3 = pot3;
	}
	@XmlElement
	public String getBtn4() {
		return btn4;
	}
	public void setBtn4(String btn4) {
		this.btn4 = btn4;
	}
	@XmlElement
	public String getBtn5() {
		return btn5;
	}
	public void setBtn5(String btn5) {
		this.btn5 = btn5;
	}
	@XmlElement
	public String getBtn6() {
		return btn6;
	}
	public void setBtn6(String btn6) {
		this.btn6 = btn6;
	}
	@XmlElement
	public String getBtn7() {
		return btn7;
	}
	public void setBtn7(String btn7) {
		this.btn7 = btn7;
	}
	
	@Override
	public String toString(){
		return "led0: "+led0+" - led1: "+led1+" - led2: "+led2+" - led3: "+led3+" - led4: "+led4+" - led5: "+led5+" - led6: "+led6+" - led7: "+led7+
				" - pot0: "+pot0+" - pot1: "+pot1+" - pot2: "+pot2+" - pot3: "+pot3+
				" - btn4: "+btn4+" - btn5: "+btn5+" - btn6: "+btn6+" - btn7: "+btn7;
	}
	
}
