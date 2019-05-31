package com.zaynjarvis.assembler;

import java.io.IOException;

public class Main {
	static public void main(String[] args) throws IOException {
		String fileName = "";
		if (args.length > 0)
			fileName = args[0];
		else
			throw new Error("Should provide a file name to compile.");

		Process process = new Process(fileName);
		process.execute();
	}
}
