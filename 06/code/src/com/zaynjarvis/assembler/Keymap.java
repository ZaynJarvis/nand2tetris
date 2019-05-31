package com.zaynjarvis.assembler;

import java.util.*;
import java.io.*;

class Keymap {
	Map<String, String> prop = new HashMap<>();

	Keymap() throws IOException {
		init();
	}

	private void init() throws IOException {
		Keyword kw = new Keyword();
		prop = kw.getMap();
	}

	public boolean hasKeyword(String keyword) {
		return prop.getOrDefault(keyword, "-1") != "-1";
	}

	public int getAddress(String keyword) {
		return Integer.parseInt(prop.get(keyword), 10);
	}

	public int setAddress(String keyword, String value) {
		prop.put(keyword, value);
		return Integer.parseInt(value);
	}

	public int setAddress(String keyword, int value) {
		prop.put(keyword, Integer.toString(value));
		return value;
	}

	@Override
	public String toString() {
		return prop.toString();
	}
}
