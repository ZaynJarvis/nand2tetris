package com.zaynjarvis.vm.translator;

class ParserController {
  static String start() {
    return Parser.parseBootstrap();
  }

  static String end() {
    return Parser.parseEndLoop();
  }

  static String parse(Token token) {
    if (token.isBiperandALU())
      return Parser.parseBiperandALU(token.operator);
    else if (token.isCompare())
      return Parser.parseCompare(token.operator);
    else if (token.isUperandALU())
      return Parser.parseUperand(token.operator);
    else if (token.isLabel())
      return Parser.parseLabel(token.label);
    else if (token.isBranch())
      return Parser.parseBranch(token.label);
    else if (token.isConditionalBranch())
      return Parser.parseConditionalBranch(token.label);
    else if (token.isFunctionCall())
      return Parser.parseFunctionCall(token.function, token.argn);
    else if (token.isFunctionDeclaration())
      return Parser.parseFunctionDeclaration(token.function, token.argn);
    else if (token.isFunctionReturn())
      return Parser.parseFunctionReturn();
    else if (token.isPush()) {
      switch (token.segment) {
      case "constant":
        return Parser.parsePushConstant(token.offset);
      case "pointer":
        return Parser.parsePushPointer(token.offset);
      case "temp":
        return Parser.parsePushTemp(token.offset);
      case "static":
        return Parser.parsePushStatic(token.offset);
      default:
        return Parser.parsePushReference(token.segment, token.offset);
      }
    } else if (token.isPop()) {
      switch (token.segment) {
      case "pointer":
        return Parser.parsePopPointer(token.offset);
      case "temp":
        return Parser.parsePopTemp(token.offset);
      case "static":
        return Parser.parsePopStatic(token.offset);
      default:
        return Parser.parsePopReference(token.segment, token.offset);
      }
    } else {
      throw new Error("Request not satisfied, function " + token + " not implemented yet");
    }
  }
}