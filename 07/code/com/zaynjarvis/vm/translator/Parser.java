package com.zaynjarvis.vm.translator;

final class Parser {
  // Parsing compare block for eq / lt / gt / le / ge
  // @SP
  // AM=M-1
  // D=M
  // A=A-1
  // D=M-D
  // @labelT
  // D;J**
  // D=0
  // @labelF
  // 0;JMP
  // (labelT)
  // D=-1
  // (labelF)
  // @SP
  // A=M-1
  // M=D
  static String parseCompare(String condition) {
    return String.format(
        "@SP\nAM=M-1\nD=M\nA=A-1\nD=M-D\n@%2$s\nD;J%1$s\nD=0\n@%3$s\n0;JMP\n(%2$s)\nD=-1\n(%3$s)\n@SP\nA=M-1\nM=D\n",
        condition.toUpperCase(), Util.getNewLabel(), Util.getNewLabel());
  }

  // Parsing A/L block for + / - / & / |
  // @SP
  // AM=M-1
  // D=M
  // A=A-1
  // D=M*D
  // M=D
  static String parseBiperandALU(String operator) {
    return String.format("@SP\nAM=M-1\nD=M\nA=A-1\nD=M%sD\nM=D\n", Util.opMap.get(operator));
  }

  // Parsing uni-operand block for - / !
  // @SP
  // A=M-1
  // D=M
  // M=*D
  static String parseUperand(String operator) {
    return String.format("@SP\nA=M-1\nD=M\nM=%sD\n", Util.opMap.get(operator));
  }

  // Parsing push command for temp / static
  // @loc
  // D=M
  // @SP
  // AM=M+1
  // A=A-1
  // M=D
  static String parsePushValue(String segment, int offset) {
    return String.format("@%d\nD=M\n@SP\nAM=M+1\nA=A-1\nM=D\n", Util.addressMap.get(segment) + offset);
  }

  // Parsing push command for local / argument / this / that
  // @*
  // D=A
  // @*
  // A=M
  // A=A+D
  // D=M
  // @SP
  // AM=M+1
  // A=A-1
  // M=D
  static String parsePushReference(String segment, int offset) {
    return String.format("@%2$s\nD=A\n@%1$d\nA=M\nA=A+D\nD=M\n@SP\nAM=M+1\nA=A-1\nM=D\n", Util.addressMap.get(segment),
        offset);
  }

  // Parsing push command for constant
  // @*
  // D=A
  // @SP
  // AM=M+1
  // A=A-1
  // M=D
  static String parsePushConstant(int value) {
    return String.format("@%d\nD=A\n@SP\nAM=M+1\nA=A-1\nM=D\n", value);
  }

  // Parsing push command for pointer (point to this / that)
  // @4
  // D=M
  // @SP
  // AM=M+1
  // A=A-1
  // M=D
  static String parsePushPointer(int offset) {
    return String.format("@%d\nD=M\n@SP\nAM=M+1\nA=A-1\nM=D\n", 3 + offset);
  }

  // Parsing pop command for temp / static
  // @SP
  // AM=M-1
  // D=M
  // @*
  // M=D
  static String parsePopValue(String segment, int offset) {
    return String.format("@SP\nAM=M-1\nD=M\n@%d\nM=D\n", Util.addressMap.get(segment) + offset);
  }

  // Parsing pop command for local / argument / this / that
  // Here I use R15 as temp register; since R15 will not be used by .vm
  // @*
  // D=A
  // @*
  // A=M
  // D=A+D
  // @15
  // M=D
  // --
  // @SP
  // AM=M-1
  // D=M
  // @15
  // A=M
  // M=D
  static String parsePopReference(String segment, int offset) {
    String getVal = String.format("@%2$d\nD=A\n@%1$d\nA=M\nD=A+D\n@15\nM=D\n", Util.addressMap.get(segment), offset);
    String popVal = "@SP\nAM=M-1\nD=M\n@15\nA=M\nM=D\n";
    return getVal + popVal;
  }

  // Parsing pop command for pointer (this / that)
  // @SP
  // AM=M-1
  // D=M
  // @*
  // M=D
  static String parsePopPointer(int offset) {
    return String.format("@SP\nAM=M-1\nD=M\n@%d\nM=D\n", 3 + offset);
  }

  // Infinity Loop mark as the end of process
  static String parseEndLoop() {
    return "(END)\n@END\n0;JMP\n";
  }
}