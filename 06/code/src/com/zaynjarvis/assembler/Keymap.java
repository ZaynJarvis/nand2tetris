package com.zaynjarvis.assembler;

import java.util.*;
import java.io.*;

class Keymap {
	Properties prop = new Properties();

	Keymap() throws IOException {
		init();
	}

	private void init() throws IOException {
		prop.load(new BufferedReader(new FileReader("./com/zaynjarvis/assembler/keyword.properties")));
	}

	public boolean hasKeyword(String keyword) {
		return prop.getProperty(keyword, "-1") != "-1";
	}

	public int getAddress(String keyword) {
		return Integer.parseInt(prop.getProperty(keyword), 10);
	}

	public int setAddress(String keyword, String value) {
		prop.setProperty(keyword, value);
		return Integer.parseInt(value);
	}

	public int setAddress(String keyword, int value) {
		prop.setProperty(keyword, Integer.toString(value));
		return value;
	}

	@Override
	public String toString() {
		return prop.toString();
	}
}
