package io.github.codeutilities.schem.utils;

public class DFText {
	
	public String text;
	
	public DFText(String text) {
		this.text = text;
	}
	
	public String asJson() {
		return "{\"id\":\"txt\",\"data\":{\"name\":\"" + this.text + "\"}}";
	}

}
