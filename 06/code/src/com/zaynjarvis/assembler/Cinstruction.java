package com.zaynjarvis.assembler;

class Cinstruction {
  private String ALUCode = "000000";
  private String destCode = "000";
  private String jmpCode = "000";

  Cinstruction(String input) {
    analyser(input);
  }

  private void analyser(String input) {
    String cropDestCode = input;
    String ALUCommand = input;
    if (input.contains("=")) {
      String[] splitDest = input.split("=");
      cropDestCode = splitDest[1].trim();
      ALUCommand = cropDestCode;
      getDestCode(splitDest[0].trim());
    }
    if (cropDestCode.contains(";")) {
      String[] splitJmp = cropDestCode.split(";");
      ALUCommand = splitJmp[0].trim();
      getJmpCode(splitJmp[1].trim());
    }
    getALUCode(ALUCommand);
  }

  private void getDestCode(String destCommand) {
    String[] destCodeArr = { "0", "0", "0" };
    if (destCommand.contains("A"))
      destCodeArr[0] = "1";
    if (destCommand.contains("D"))
      destCodeArr[1] = "1";
    if (destCommand.contains("M"))
      destCodeArr[2] = "1";
    destCode = String.join("", destCodeArr);
  }

  private void getALUCode(String ALUCommand) {
    String mSource = "0";
    if (ALUCommand.contains("M")) {
      mSource = "1";
      ALUCommand = ALUCommand.replace("M", "A");
    }
    switch (ALUCommand) {
    case "0":
      ALUCode = "101010";
      break;
    case "1":
      ALUCode = "111111";
      break;
    case "-1":
      ALUCode = "111010";
      break;
    case "D":
      ALUCode = "001100";
      break;
    case "A":
      ALUCode = "110000";
      break;
    case "!D":
      ALUCode = "001101";
      break;
    case "!A":
      ALUCode = "110001";
      break;
    case "-D":
      ALUCode = "001111";
      break;
    case "-A":
      ALUCode = "110011";
      break;
    case "D+1":
      ALUCode = "011111";
      break;
    case "A+1":
      ALUCode = "110111";
      break;
    case "D-1":
      ALUCode = "001110";
      break;
    case "A-1":
      ALUCode = "110010";
      break;
    case "D+A":
      ALUCode = "000010";
      break;
    case "D-A":
      ALUCode = "010011";
      break;
    case "A-D":
      ALUCode = "000111";
      break;
    case "D&A":
      ALUCode = "000000";
      break;
    case "D|A":
      ALUCode = "010101";
      break;
    }

    ALUCode = mSource + ALUCode;
  }

  private void getJmpCode(String JmpCommand) {
    switch (JmpCommand) {
    case "JGT":
      jmpCode = "001";
      break;
    case "JEQ":
      jmpCode = "010";
      break;
    case "JGE":
      jmpCode = "011";
      break;
    case "JLT":
      jmpCode = "100";
      break;
    case "JNE":
      jmpCode = "101";
      break;
    case "JLE":
      jmpCode = "110";
      break;
    case "JMP":
      jmpCode = "111";
      break;
    }
  }

  public String generateCode() {
    return "111" + ALUCode + destCode + jmpCode;
  }
}