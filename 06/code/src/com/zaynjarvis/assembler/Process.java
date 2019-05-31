package com.zaynjarvis.assembler;

import java.util.*;
import java.io.*;

class Process {
	int pc = -1, reg = 16;
	List<String> insList;
	String fileName;
	Scanner sc;
	Keymap km;
	File fout;

	Process(String srcFileName) throws IOException {
		fileName = srcFileName;
		km = new Keymap();
		fout = new File(fileName.replace(".asm", ".hack"));
		sc = new Scanner(new FileReader(new File(fileName)));
		insList = new ArrayList<>();
	}

	protected void execute() throws IOException {
		readCommand();
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout)))) {
			for (String command : insList) {
				String result = command.charAt(0) == '@' ? aInsHandler(command) : cInsHandler(command);
				bw.write(result + "\n");
			}
		}
	}

	private void readCommand() throws IOException {
		while (sc.hasNextLine()) {
			String input = sc.nextLine().trim().replace("\n", "").replace("\r", "");

			// Delete comment and empty line
			if (input.startsWith("/*")) {
				while (!sc.nextLine().contains("*/"))
					;
			}
			input = input.split("//")[0];
			if (input.isEmpty())
				continue;

			// allocate address for labels
			if (input.charAt(0) == '(') {
				km.setAddress(input.substring(1, input.length() - 1), pc + 1);
				continue;
			}

			pc++;
			input = input.replace("\n", "").replace("\r", "");
			insList.add(input);
		}
		sc.close();
	}

	private String aInsHandler(String input) {
		int value;
		String eff = input.substring(1);
		try {
			value = Integer.parseInt(eff);
		} catch (NumberFormatException e) {
			if (km.hasKeyword(eff))
				value = km.getAddress(eff);
			else
				value = km.setAddress(eff, reg++);
		}
		return String.format("%16s", Integer.toBinaryString(value)).replace(" ", "0");
	}

	private String cInsHandler(String input) {
		Cinstruction ins = new Cinstruction(input);
		return ins.generateCode();
	}
}
