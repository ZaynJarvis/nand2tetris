package com.zaynjarvis.compiler.model;

public class Symbol {
  static char[] symbols = { '{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=',
      '~' };

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

  @Override
  public String toString() {
    return super.toString();
  }
}
