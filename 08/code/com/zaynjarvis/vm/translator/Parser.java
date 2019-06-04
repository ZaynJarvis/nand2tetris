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

  // Parsing push command for temp
  // @*
  // D=M
  // @SP
  // AM=M+1
  // A=A-1
  // M=D
  static String parsePushTemp(int offset) {
    return String.format("@%d\nD=M\n@SP\nAM=M+1\nA=A-1\nM=D\n", 5 + offset);
  }

  // Parsing push command for static
  // @*
  // D=M
  // @SP
  // AM=M+1
  // A=A-1
  // M=D
  static String parsePushStatic(int offset) {
    return String.format("@%s.%d\nD=M\n@SP\nAM=M+1\nA=A-1\nM=D\n", Util.getClassName(), offset);
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

  // Parsing pop command for temp
  // @SP
  // AM=M-1
  // D=M
  // @*
  // M=D
  static String parsePopTemp(int offset) {
    return String.format("@SP\nAM=M-1\nD=M\n@%d\nM=D\n", 5 + offset);
  }

  // Parsing pop command for static
  // @SP
  // AM=M-1
  // D=M
  // @*
  // M=D
  static String parsePopStatic(int offset) {
    return String.format("@SP\nAM=M-1\nD=M\n@%s.%d\nM=D\n", Util.getClassName(), offset);
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

  // Parse label
  // (*)
  static String parseLabel(String label) {
    return String.format("(%s)\n", label);
  }

  // Parse Unconditional Branch
  // @*
  // 0;JMP
  static String parseBranch(String label) {
    return String.format("@%s\n0;JMP\n", label);

  }

  // Parse Conditional Branch
  // In this design, any value larger than zero are considered True. ***
  // @SP
  // AM=M-1
  // D=M
  // @*
  // D;JNE
  static String parseConditionalBranch(String label) {
    return String.format("@SP\nAM=M-1\nD=M\n@%s\nD;JNE\n", label);
  }

  // Parse Function Declaration [./functionMock.asm]
  static String parseFunctionDeclaration(String functionName, int argn) {
    StringBuilder sb = new StringBuilder();
    String setLCLAddress = String.format("(%s)\n@SP\nD=M\n@LCL\nM=D\n", functionName);
    Util.setClassName(functionName);

    sb.append(setLCLAddress);
    sb.append("@SP\nA=M\n");
    for (int i = 0; i < argn; i++) {
      sb.append("M=0\nA=A+1\n");
    }
    sb.append("D=A\n@SP\nM=D\n");
    return sb.toString();
  }

  // Parse Function Call [./functionMock.asm]
  static String parseFunctionCall(String functionName, int argn) {
    String funcReturnName = Util.getFunctionReturn(functionName);

    String storeARGtoTemp = String.format("@%d\nD=A\n@SP\nD=M-D\n@15\nM=D\n", argn);
    String setReturnAddress = String.format("@%s\nD=A\n@SP\nA=M\nM=D\n", funcReturnName);
    String dumpFrame = "@LCL\nD=M\n@SP\nAM=M+1\nM=D\n@ARG\nD=M\n@SP\nAM=M+1\nM=D\n@THIS\nD=M\n@SP\nAM=M+1\nM=D\n@THAT\nD=M\n@SP\nAM=M+1\nM=D\n";
    String updateARG = "@15\nD=M\n@ARG\nM=D\n";
    String increseSP = "@SP\nM=M+1\n";
    String jumpToFunc = String.format("@%s\n0;JMP\n(%s)\n", functionName, funcReturnName);
    return storeARGtoTemp + setReturnAddress + dumpFrame + updateARG + increseSP + jumpToFunc;
  }

  // Parse Function Return [./functionMock.asm]
  // R13 to temperary store frame address
  // R14 to store ARG address
  // R15 to store return address
  static String parseFunctionReturn() {
    String storeTempARG = "@ARG\nD=M\n@14\nM=D\n";
    String restorePointer = "@5\nD=A\n@LCL\nD=M-D\n@13\nM=D\nA=D\nD=M\n@15\nM=D\n@13\nAM=M+1\nD=M\n@1\nM=D\n@13\nAM=M+1\nD=M\n@2\nM=D\n@13\nAM=M+1\nD=M\n@3\nM=D\n@13\nAM=M+1\nD=M\n@4\nM=D\n";
    String returnValueSP = "@SP\nA=M-1\nD=M\n@14\nA=M\nM=D\nD=A\n@SP\nM=D+1\n";
    String returnLabel = "@15\nA=M\n0;JMP\n";
    return storeTempARG + restorePointer + returnValueSP + returnLabel;
  }

  // Startup code for calling Sys.init
  static String parseBootstrap() {
    return "@Sys.init\n0;JMP\n";
  }

  // Infinity Loop mark as the end of process
  static String parseEndLoop() {
    return "(END)\n@END\n0;JMP\n";
  }
}