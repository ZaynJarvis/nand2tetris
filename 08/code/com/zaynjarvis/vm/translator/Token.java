package com.zaynjarvis.vm.translator;

class Token {
  private String _token;
  String operator;
  String segment;
  String label;
  String function;
  int argn;
  int localn;
  int offset;

  Token(String token) {
    _token = token;
    init(token);
  }

  private void init(String token) {
    String[] arr = token.split(" ");
    operator = arr[0];
    if (arr.length > 1) {
      String temp = arr[1];
      label = temp;
      function = temp;
      segment = temp;
    }
    if (arr.length > 2) {
      int temp = Integer.parseInt(arr[2]);
      offset = temp;
      argn = temp;
      localn = temp;
    }
  }

  boolean isCompare() {
    return ("eq".equals(operator)) || ("lt".equals(operator)) || ("gt".equals(operator)) || ("le".equals(operator))
        || ("ge".equals(operator));
  }

  boolean isBiperandALU() {
    return ("add".equals(operator)) || ("sub".equals(operator)) || ("and".equals(operator)) || ("or".equals(operator));
  }

  boolean isUperandALU() {
    return ("not".equals(operator)) || ("neg".equals(operator));
  }

  boolean isPush() {
    return "push".equals(operator);
  }

  boolean isPop() {
    return "pop".equals(operator);
  }

  boolean isLabel() {
    return "label".equals(operator);
  }

  boolean isBranch() {
    return "goto".equals(operator);
  }

  boolean isConditionalBranch() {
    return "if-goto".equals(operator);
  }

  boolean isFunctionDeclaration() {
    return "function".equals(operator);
  }

  boolean isFunctionCall() {
    return "call".equals(operator);
  }

  boolean isFunctionReturn() {
    return "return".equals(operator);
  }

  @Override
  public String toString() {
    return "Token [" + _token + "]";
  }
}