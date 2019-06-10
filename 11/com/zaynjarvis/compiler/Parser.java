package com.zaynjarvis.compiler;

import com.zaynjarvis.compiler.model.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

class Parser {
  ListIterator<Token> tokens;
  int padding;
  String className;
  String functionName;
  SymbolTable st;
  VMWriter vw;

  Parser(List<Token> tokens, BufferedWriter bw) {
    this.tokens = tokens.listIterator();
    vw = new VMWriter(bw);
    st = new SymbolTable();
    padding = 0;
  }

  void parse() throws IOException {
    while (tokens.hasNext()) {
      parserGateway(tokens.next());
    }
  }

  private void parserGateway(Token token) throws IOException {
    String value = token.getValue();
    tokens.previous();
    if ("class".equals(value)) {
      parseClass();
    } else if ("static".equals(value) || "field".equals(value) || "var".equals(value)) {
      parseClassVarDec();
    } else if ("static".equals(value) || "field".equals(value) || "var".equals(value)) {
      parseVarDec();
    } else if ("constructor".equals(value) || "function".equals(value) || "method".equals(value)) {
      parseSubroutineDec();
    } else if ("let".equals(value)) {
      parseLetStatement();
    } else if ("if".equals(value)) {
      parseIfStatement();
    } else if ("while".equals(value)) {
      parseWhileStatement();
    } else if ("do".equals(value)) {
      parseDoStatement();
    } else if ("return".equals(value)) {
      parseReturnStatement();
    } else {
      throw new Error("invalid input:" + token);
    }
  }

  private void parseClass() throws IOException {
    tokens.next();
    Token className = tokens.next();
    this.className = className.getValue();
    tokens.next();
    Token newToken = tokens.next();
    while (!newToken.getValue().equals("}")) {
      parserGateway(newToken);
      newToken = tokens.next();
    }
  }

  private void parseVarDec() throws IOException {
    tokens.next();
    Token type = tokens.next();
    Token name = tokens.next();
    st.define(new Symbol(name, type, new Token("keyword", "local")));
    Token newToken = tokens.next();
    while (!newToken.getValue().equals(";")) {
      Token newVar = tokens.next();
      st.define(new Symbol(newVar, type, new Token("keyword", "local")));
      newToken = tokens.next(); // ; / ,
    }
  }

  private void parseClassVarDec() throws IOException {
    Token kind = tokens.next();
    Token type = tokens.next();
    Token name = tokens.next();
    st.define(new Symbol(name, type, kind));
    Token newToken = tokens.next();
    while (!newToken.getValue().equals(";")) {
      Token newVar = tokens.next();
      st.define(new Symbol(newVar, type, kind));
      newToken = tokens.next(); // , \ ;
    }
  }

  private void parseSubroutineDec() throws IOException {
    st.startSubroutine();
    Token kind = tokens.next();
    if (kind.isMethod()) {
      st.define(new Symbol("this", className, "argument"));
    }
    tokens.next(); // return type - check for void here
    Token functionName = tokens.next();
    this.functionName = className + "." + functionName.getValue();
    tokens.next(); // (
    parseParameterList();
    parseSubroutineBody(kind.isMethod());
  }

  private void parseParameterList() throws IOException {
    Token paramToken = tokens.next();
    while (!paramToken.getValue().equals(")")) {
      if (paramToken.getValue().equals(","))
        paramToken = tokens.next(); // indicate the arg type
      Token name = tokens.next();
      st.define(new Symbol(name, paramToken, new Token("keyword", "argument")));
      paramToken = tokens.next();
    }
  }

  private void parseSubroutineBody(boolean isMethod) throws IOException {
    tokens.next(); // {
    Token newToken = tokens.next();
    boolean statementsCreated = false;
    while (!newToken.getValue().equals("}")) {
      if (!newToken.getValue().equals("var") && !statementsCreated) {
        vw.writeFunction(this.functionName, st.varCount("local"));
        if (isMethod) {
          vw.writePush("argument", 0);
          vw.writePop("pointer", 0);
        }
        statementsCreated = true;
      }
      parserGateway(newToken);
      newToken = tokens.next();
    }
  }

  private void parseStatements() throws IOException {
    Token newToken = tokens.next();
    while (!newToken.getValue().equals("}")) {
      parserGateway(newToken);
      newToken = tokens.next();
    }
  }

  private void parseTerm() throws IOException {
    Token newToken = tokens.next();
    String tokenValue = newToken.getValue();
    if (newToken.isUperator()) {
      parseTerm();
      vw.writeALU(tokenValue.equals("~") ? "not" : "neg");
    } else if (newToken.isKeywordConstant()) {
      if (tokenValue.equals("this"))
        vw.writePush("argument", 0);
      else
        vw.writePush("constant", Util.keyword2Int(tokenValue));
    } else if (newToken.getkey().endsWith("intConst")) {
      vw.writePush("constant", Integer.parseInt(tokenValue));
    } else if (newToken.getkey().endsWith("stringConst")) {
      int length = tokenValue.length();
      vw.writePush("constant", length);
      vw.writeCall("String.new", 1); // push constant val
      for (char ch : tokenValue.toCharArray()) {
        vw.writePush("constant", ch);
        vw.writeCall("String.appendChar", 1);
      }
    } else if (newToken.getValue().equals("(")) {
      // New token is '(' here
      parseExpression();
      tokens.next(); // )
    } else {
      Token lookAhead = tokens.next();
      if (lookAhead.getValue().equals(".") || lookAhead.getValue().equals("(")) {
        tokens.previous();
        tokens.previous();
        parseSubroutineCall();
      } else if (lookAhead.getValue().equals("[")) {
        Symbol arr = st.getSymbol(newToken.getValue()); // arrayName
        vw.writePush(arr.getKind(), arr.getIndex());
        parseExpression(); // arrayIndex
        vw.writeALU("add");
        vw.writePop("pointer", 1);
        vw.writePush("that", 0);
        tokens.next(); // ]
      } else {
        tokens.previous(); // exclude look ahead
        Symbol var = st.getSymbol(newToken.getValue());
        vw.writePush(var.getKind(), var.getIndex());
      }
    }
  }

  private void parseExpression() throws IOException {
    parseTerm();
    Token newToken = tokens.next();
    while (newToken.isOperator()) {
      parseTerm();
      String operator = newToken.getValue();
      if (operator.equals("*"))
        vw.writeCall("Math.multiply", 2);
      else if (operator.equals("/"))
        vw.writeCall("Math.divide", 2);
      else
        vw.writeALU(Util.getOperatorCommand(operator));
      newToken = tokens.next();
    }
    tokens.previous();
  }

  private int parseExpressionList() throws IOException {
    Token paramToken = tokens.next();
    int count = 0;
    while (!paramToken.getValue().equals(")")) {
      if (paramToken.getValue().equals(",")) {
        parseExpression();
      } else {
        tokens.previous(); // Go back to the entry point of expression
        parseExpression();
      }
      paramToken = tokens.next();
      count++;
    }
    return count;
  }

  private void parseSubroutineCall() throws IOException {
    String className, functionName;
    Token temp = tokens.next(); // firstName
    Token newToken = tokens.next();
    boolean isMethod = false;
    if (newToken.getValue().equals(".")) {
      className = temp.getValue();
      functionName = className + "." + tokens.next().getValue();
      newToken = tokens.next(); // (
    } else {
      className = "";
      functionName = temp.getValue();
      isMethod = true;
    }
    if (className.isEmpty()) {
      vw.writePush("pointer", 0);
    } else if (st.contains(className)) {
      Symbol classVar = st.getSymbol(className);
      vw.writePush(classVar.getKind(), classVar.getIndex());
      isMethod = true;
    }
    int count = parseExpressionList();
    vw.writeCall(functionName, isMethod ? count + 1 : count);
  }

  private void parseLetStatement() throws IOException {
    boolean isArray = false;
    tokens.next(); // let
    Symbol dest = st.getSymbol(tokens.next().getValue());
    Token newToken = tokens.next(); // = / [
    if (!newToken.getValue().equals("=")) {
      isArray = true;
      // newToken = [
      vw.writePush(dest.getKind(), dest.getIndex());
      parseExpression();
      tokens.next(); // ]
      vw.writeALU("add");
      newToken = tokens.next(); // =
    }
    parseExpression();
    vw.writePop("temp", 0);
    if (isArray) {
      vw.writePop("pointer", 1);
      vw.writePush("temp", 0);
      vw.writePop("that", 0);
    } else {
      vw.writePush("temp", 0);
      vw.writePop(dest.getKind(), dest.getIndex());
    }
    tokens.next(); // ;
  }

  private void parseIfStatement() throws IOException {
    tokens.next(); // if
    tokens.next(); // (
    parseExpression();
    tokens.next(); // )
    tokens.next(); // {
    vw.writeALU("not");
    String elseLabel = Util.newLabel(functionName, "ELSE");
    vw.writeIf(elseLabel);
    parseStatements();
    vw.writeLabel(elseLabel);
    Token newToken = tokens.next();
    if (newToken.getValue().equals("else")) {
      String endLabel = Util.newLabel(functionName, "END");
      vw.writeGoto(endLabel);
      tokens.next(); // {
      parseStatements();
      vw.writeGoto(endLabel);
    } else {
      tokens.previous();
    }
  }

  private void parseWhileStatement() throws IOException {
    tokens.next(); // while
    String whileLabel = Util.newLabel(functionName, "WHILE");
    vw.writeLabel(whileLabel);
    tokens.next(); // (
    parseExpression();
    vw.writeALU("not");
    String endLabel = Util.newLabel(functionName, "END");
    vw.writeIf(endLabel);
    tokens.next(); // )
    tokens.next(); // {
    parseStatements();
    vw.writeGoto(whileLabel);
    vw.writeLabel(endLabel);
  }

  private void parseDoStatement() throws IOException {
    tokens.next(); // do
    parseSubroutineCall();
    tokens.next(); // ;
    vw.writePop("temp", 0);
  }

  private void parseReturnStatement() throws IOException {
    tokens.next(); // return
    Token newToken = tokens.next();
    if (!newToken.getValue().equals(";")) {
      tokens.previous();
      parseExpression();
      newToken = tokens.next(); // ;
    } else {
      vw.writePush("constant", 0);
    }
  }
}