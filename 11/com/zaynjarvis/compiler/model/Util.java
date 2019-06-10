package com.zaynjarvis.compiler.model;

public class Util {
  static char[] symbols = { '{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=',
      '~' };
  static String[] keywords = { "class", "constructor", "function", "method", "field", "static", "var", "int", "char",
      "boolean", "void", "true", "false", "null", "this", "let", "do", "if", "else", "while", "return" };
  static int labelCount = 0;

  public static String newLabel(String functionName, String condition) {
    return String.format("%s.%s.%d", functionName, condition, labelCount++);
  }

  public static String newLabel(String functionName) {
    return String.format("%s.%d", functionName, labelCount++);
  }

  public static boolean isSymbol(char symbol) {
    boolean found = false;
    for (char s : symbols) {
      if (s == symbol) {
        found = true;
        break;
      }
    }
    return found;
  }

  public static boolean isKeyword(String word) {
    boolean found = false;
    for (String s : keywords) {
      if (s.equals(word)) {
        found = true;
        break;
      }
    }
    return found;
  }

  public static int keyword2Int(String keyword) {
    switch (keyword) {
    case "true":
      return -1;
    case "false":
    case "null":
      return 0;
    default:
      throw new Error("Unexpected Keyword: " + keyword);
    }
  }

  public static String getOperatorCommand(String operator) {
    switch (operator) {
    case "+":
      return "add";
    case "-":
      return "sub";
    case "=":
      return "eq";
    case "<":
      return "lt";
    case ">":
      return "gt";
    case "&":
      return "and";
    case "|":
      return "or";
    default:
      throw new Error("Unexpected Operator: " + operator);
    }
  }
}
