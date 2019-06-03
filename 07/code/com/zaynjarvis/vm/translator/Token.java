package com.zaynjarvis.vm.translator;

class Token {
  private String _token;
  String operator;
  String segment;
  int offset;

  Token(String token) {
    _token = token;
    init(token);
  }

  private void init(String token) {
    String[] arr = token.split(" ");
    operator = arr[0];
    if (arr.length > 1) {
      segment = arr[1];
      offset = Integer.parseInt(arr[2]);
    }
  }

  boolean isCompare() {
    return ("eq".equals(operator)) || ("lt".equals(operator)) || ("gt".equals(operator)) || ("le".equals(operator))
        || ("ge".equals(operator));
  }

  boolean isBiperandALU() {
    return ("add".equals(operator)) || ("sub".equals(operator)) || ("and".equals(operator)) || ("or".equals(operator));
  }

  boolean isUperand() {
    return ("not".equals(operator)) || ("neg".equals(operator));
  }

  boolean isPush() {
    return "push".equals(operator);
  }

  boolean isPop() {
    return "pop".equals(operator);
  }

  @Override
  public String toString() {
    return "Token [" + _token + "]";
  }
}