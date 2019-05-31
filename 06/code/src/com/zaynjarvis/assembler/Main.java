package com.zaynjarvis.assembler;

import java.io.IOException;

public class Main {
	static public void main(String[] args) throws IOException {
		String fileName = "./test.asm";
		try {
			fileName = args[0];
		} catch (IndexOutOfBoundsException e) {
		}
		Process process = new Process();
		process.execute(fileName);
	}
}
