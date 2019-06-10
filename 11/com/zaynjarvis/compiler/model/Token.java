package com.zaynjarvis.compiler.model;

public class Token {
  private String key;
  private String value;

  public Token(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getkey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public boolean isOperator() {
    char ch = this.value.charAt(0);
    return ch == '+' || ch == '-' || ch == '=' || ch == '<' || ch == '>' || ch == '*' || ch == '/' || ch == '&'
        || ch == '|';
  }

  public boolean isUperator() {
    char ch = this.value.charAt(0);
    return ch == '-' || ch == '~';
  }

  public boolean isKeywordConstant() {
    String value = this.value;
    return value.equals("true") || value.equals("false") || value.equals("null") || value.equals("this");
  }

  public boolean isConstant() {
    return this.isKeywordConstant() || key.equals("stringConst") || key.equals("intConst");
  }

  public boolean isMethod() {
    return this.value.equals("method");
  }

  public boolean isConstructor() {
    return this.value.equals("constructor");
  }

  public boolean isFunction() {
    return this.value.equals("function");
  }

  public Token correctOperator() {
    if (value.equals("<"))
      return new Token("symbol", "&lt;");
    else if (value.equals(">"))
      return new Token("symbol", "&gt;");
    else if (value.equals("&"))
      return new Token("symbol", "&amp;");
    else
      return this;
  }

  // @Override
  // public String toString() {
  // return String.format("$1$" + this.padding * 2 + "s<%2$s> %3$s </%2$s>\n",
  // "", key, value);
  // }

  @Override
  public String toString() {
    return String.format("<%1$s> %2$s </%1$s>\n", key, value);
  }
}