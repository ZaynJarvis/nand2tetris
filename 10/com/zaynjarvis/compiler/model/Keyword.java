package com.zaynjarvis.compiler.model;

public class Keyword {
  static String[] keywords = { "class", "constructor", "function", "method", "field", "static", "var", "int", "char",
      "boolean", "void", "true", "false", "null", "this", "let", "do", "if", "else", "while", "return" };

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
}
