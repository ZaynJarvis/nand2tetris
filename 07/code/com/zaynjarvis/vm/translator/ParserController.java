package com.zaynjarvis.vm.translator;

class ParserController {
  static String end() {
    return Parser.parseEndLoop();
  }

  static String parse(Token token) {
    if (token.isBiperandALU())
      return Parser.parseBiperandALU(token.operator);
    else if (token.isCompare())
      return Parser.parseCompare(token.operator);
    else if (token.isUperand())
      return Parser.parseUperand(token.operator);
    else if (token.isPush()) {
      switch (token.segment) {
      case "constant":
        return Parser.parsePushConstant(token.offset);
      case "pointer":
        return Parser.parsePushPointer(token.offset);
      case "temp":
      case "static":
        return Parser.parsePushValue(token.segment, token.offset);
      default:
        return Parser.parsePushReference(token.segment, token.offset);
      }
    } else if (token.isPop()) {
      switch (token.segment) {
      case "pointer":
        return Parser.parsePopPointer(token.offset);
      case "temp":
      case "static":
        return Parser.parsePopValue(token.segment, token.offset);
      default:
        return Parser.parsePopReference(token.segment, token.offset);
      }
    } else {
      throw new Error("Request not satisfied, function " + token + " not implemented yet");
    }
  }
}