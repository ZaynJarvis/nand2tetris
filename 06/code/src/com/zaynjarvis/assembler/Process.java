package com.zaynjarvis.assembler;

import java.util.*;
import java.io.*;

class Process {
	int pc = -1;
	int reg = 16;
	Keymap km;

	protected void execute(String fileName) throws IOException {
		Scanner sc = new Scanner(new FileReader(new File(fileName)));
		File fout = new File(fileName.replace(".asm", ".hack"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout)));
		km = new Keymap();

		while (sc.hasNextLine()) {
			String input = sc.nextLine().trim();
			String result;

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

			// resolving instructions
			if (input.charAt(0) == '@')
				result = aInstructionWorkflow(input);
			else
				result = cInstructionWorkflow(input);

			bw.write(result);
			bw.newLine();
		}
		bw.close();
		sc.close();
	}

	private String aInstructionWorkflow(String input) {
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

	private String cInstructionWorkflow(String input) {
		Cinstruction ins = new Cinstruction(input);
		return ins.generateCode();
	}
}
