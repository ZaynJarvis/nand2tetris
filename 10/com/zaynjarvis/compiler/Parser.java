package com.zaynjarvis.compiler;

import com.zaynjarvis.compiler.model.*;
import java.util.*;

class Parser {
  ListIterator<Token> tokens;
  StringBuilder sb;
  int padding;

  Parser(List<Token> tokens) {
    this.tokens = tokens.listIterator();
    sb = new StringBuilder();
    padding = 0;
  }

  void parse() {
    while (tokens.hasNext()) {
      parserGateway(tokens.next());
    }
  }

  private void parserGateway(Token token) {
    String value = token.getValue();
    tokens.previous();
    if ("class".equals(value)) {
      parseClass();
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

  private void parseClass() {
    sb.append("<class>");
    sb.append(tokens.next());
    sb.append(tokens.next());
    sb.append(tokens.next());
    Token newToken = tokens.next();
    while (!newToken.getValue().equals("}")) {
      parserGateway(newToken);
      newToken = tokens.next();
    }
    sb.append(newToken);
    sb.append("</class>");
  }

  private void parseSubroutineDec() {
    sb.append("<subroutineDec>");
    sb.append(tokens.next());
    sb.append(tokens.next());
    sb.append(tokens.next());
    sb.append(tokens.next());
    parseParameterList();
    parseSubroutineBody();
    sb.append("</subroutineDec>");
  }

  private void parseVarDec() {
    Token keyword = tokens.next();
    String tag;

    if (keyword.getValue().equals("var"))
      tag = "varDec";
    else
      tag = "classVarDec";
    sb.append(String.format("<%s>", tag));

    sb.append(keyword);
    sb.append(tokens.next());
    sb.append(tokens.next());
    Token newToken = tokens.next();
    while (!newToken.getValue().equals(";")) {
      sb.append(newToken);
      sb.append(tokens.next());
      newToken = tokens.next();
    }
    sb.append(newToken); // ;
    sb.append(String.format("</%s>", tag));
  }

  private void parseLetStatement() {
    sb.append("<letStatement>");
    sb.append(tokens.next());
    sb.append(tokens.next());
    Token newToken = tokens.next();
    if (!newToken.getValue().equals("=")) {
      sb.append(newToken);
      parseExpression();
      sb.append(tokens.next());
      newToken = tokens.next();
    }
    sb.append(newToken); // =
    parseExpression();
    sb.append(tokens.next()); // ;
    sb.append("</letStatement>");
  }

  private void parseIfStatement() {
    sb.append("<ifStatement>");
    sb.append(tokens.next());
    sb.append(tokens.next());
    parseExpression();
    sb.append(tokens.next());
    sb.append(tokens.next()); // {
    parseStatements();
    Token newToken = tokens.next();
    if (newToken.getValue().equals("else")) {
      sb.append(newToken); // else
      sb.append(tokens.next()); // {
      parseStatements();
    } else {
      tokens.previous();
    }
    sb.append("</ifStatement>");
  }

  private void parseWhileStatement() {
    sb.append("<whileStatement>");
    sb.append(tokens.next());
    sb.append(tokens.next());
    parseExpression();
    sb.append(tokens.next());
    sb.append(tokens.next()); // {
    parseStatements();
    sb.append("</whileStatement>");
  }

  private void parseDoStatement() {
    sb.append("<doStatement>");
    sb.append(tokens.next());
    parseSubroutineCall();
    sb.append(tokens.next()); // ;
    sb.append("</doStatement>");
  }

  private void parseReturnStatement() {
    sb.append("<returnStatement>");
    sb.append(tokens.next());
    Token newToken = tokens.next();
    if (!newToken.getValue().equals(";")) {
      tokens.previous();
      parseExpression();
      newToken = tokens.next();
    }
    sb.append(newToken);
    sb.append("</returnStatement>");
  }

  private void parseStatements() {
    sb.append("<statements>");
    Token newToken = tokens.next();
    while (!newToken.getValue().equals("}")) {
      parserGateway(newToken);
      newToken = tokens.next();
    }
    sb.append("</statements>");
    sb.append(newToken);
  }

  private void parseParameterList() {
    Token paramToken = tokens.next();
    sb.append("<parameterList>");
    while (!paramToken.getValue().equals(")")) {
      sb.append(paramToken);
      paramToken = tokens.next();
    }
    sb.append("</parameterList>");
    sb.append(paramToken);
  }

  private void parseSubroutineBody() {
    sb.append("<subroutineBody>");
    sb.append(tokens.next());
    Token newToken = tokens.next();
    boolean statementsCreated = false;
    while (!newToken.getValue().equals("}")) {
      if (!newToken.getValue().equals("var") && !statementsCreated) {
        sb.append("<statements>");
        statementsCreated = true;
      }
      parserGateway(newToken);
      newToken = tokens.next();
    }
    sb.append("</statements>");
    sb.append(newToken);
    sb.append("</subroutineBody>");
  }

  private void parseExpression() {
    sb.append("<expression>");
    parseTerm();
    Token newToken = tokens.next();
    while (newToken.isOperator()) {
      sb.append(newToken.correctOperator());
      parseTerm();
      newToken = tokens.next();
    }
    tokens.previous();
    sb.append("</expression>");
  }

  private void parseExpressionList() {
    Token paramToken = tokens.next();
    sb.append("<expressionList>");
    while (!paramToken.getValue().equals(")")) {
      if (paramToken.getValue().equals(",")) {
        sb.append(paramToken);
        parseExpression();
      } else {
        tokens.previous();
        parseExpression();
      }
      paramToken = tokens.next();
    }
    sb.append("</expressionList>");
    sb.append(paramToken); // )
  }

  private void parseTerm() {
    sb.append("<term>");
    Token newToken = tokens.next();
    if (newToken.isUperator()) {
      sb.append(newToken);
      parseTerm();
    } else if (newToken.isConstant()) {
      sb.append(newToken);
    } else if (newToken.getValue().equals("(")) {
      sb.append(newToken);
      parseExpression();
      sb.append(tokens.next()); // )
    } else {
      Token lookAhead = tokens.next();
      if (lookAhead.getValue().equals(".") || lookAhead.getValue().equals("(")) {
        tokens.previous();
        tokens.previous();
        parseSubroutineCall();
      } else if (lookAhead.getValue().equals("[")) {
        sb.append(newToken);
        sb.append(lookAhead);
        parseExpression();
        sb.append(tokens.next()); // ]
      } else {
        tokens.previous();
        sb.append(newToken);
      }
    }
    sb.append("</term>");
  }

  private void parseSubroutineCall() {
    sb.append(tokens.next());
    Token newToken = tokens.next();
    if (newToken.getValue().equals(".")) {
      sb.append(newToken);
      sb.append(tokens.next());
      newToken = tokens.next();
    }
    sb.append(newToken); // (
    parseExpressionList();
  }

  @Override
  public String toString() {
    return sb.toString();
  }
}