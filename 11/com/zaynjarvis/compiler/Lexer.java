package com.zaynjarvis.compiler;

import java.util.*;
import com.zaynjarvis.compiler.model.*;

class Lexer {
  Scanner sc;
  List<Character> charList;
  List<Token> tokens;

  Lexer(Scanner sc) {
    this.sc = sc;
    this.charList = new ArrayList<>();
    this.tokens = new ArrayList<>();
    readAllChars();
    lexicalAnalysis();
  }

  public List<Token> getTokens() {
    return tokens;
  }

  private void readAllChars() {
    while (sc.hasNext()) {
      char ch = sc.next().charAt(0);
      charList.add(ch);
    }
  }

  private boolean isNum(char ch) {
    return ch > 47 && ch < 58;
  }

  private boolean isAlpha(char ch) {
    return (ch > 64 && ch < 91) || (ch > 96 && ch < 123);
  }

  private boolean isAlphaNum(char ch) {
    return isNum(ch) || isAlpha(ch);
  }

  private boolean isIDHead(char ch) {
    return isAlpha(ch) || ch == 95;
  }

  private boolean isIDBody(char ch) {
    return isAlphaNum(ch) || ch == 95;
  }

  private void lexicalAnalysis() {
    Iterator<Character> it = charList.iterator();
    char ch = 0;
    while (it.hasNext()) {
      if (ch == 0)
        ch = it.next();
      String s = Character.toString(ch);
      if (ch == '\n' || ch == '\r' || ch == '\t' || ch == ' ') {
        ch = 0;
        continue;
      } else if (ch == '/')
        ch = scanComment(it);
      else if (Util.isSymbol(ch)) {
        tokens.add(new Token("symbol", s));
        ch = 0;
      } else if (ch == '"')
        ch = scanString(it);
      else if (isNum(ch))
        ch = scanInt(it, ch);
      else if (isIDHead(ch))
        ch = scanID(it, ch);
      else {
        throw new Error("Unexpected character:" + ch);
      }
    }
  }

  private char scanComment(Iterator<Character> it) {
    if (it.hasNext()) {
      char ch = it.next();
      if (ch == '/') {
        while (it.hasNext()) {
          if (it.next() == '\n')
            return 0;
        }
      } else if (ch == '*') {
        while (it.hasNext()) {
          if (it.next() == '*' && it.next() == '/')
            return 0;
        }
      } else {
        tokens.add(new Token("symbol", "/"));
        return ch;
      }
    }
    return 0;
  }

  private char scanString(Iterator<Character> it) {
    StringBuilder sb = new StringBuilder();
    while (it.hasNext()) {
      char nextChar = it.next();
      if (nextChar == '"')
        break;
      else
        sb.append(nextChar);
    }
    tokens.add(new Token("stringConst", sb.toString()));
    return 0;
  }

  private char scanInt(Iterator<Character> it, char ch) {
    StringBuilder sb = new StringBuilder();
    sb.append(ch);
    char nextChar;
    while (it.hasNext()) {
      nextChar = it.next();
      if (isNum(nextChar))
        sb.append(nextChar);
      else if (isAlpha(nextChar)) {
        throw new Error("unexpected identifier");
      } else {
        tokens.add(new Token("intConst", sb.toString()));
        return nextChar;
      }
    }
    return 0;
  }

  private char scanID(Iterator<Character> it, char ch) {
    StringBuilder sb = new StringBuilder();
    sb.append(ch);
    char nextChar;
    while (it.hasNext()) {
      nextChar = it.next();
      if (isIDBody(nextChar))
        sb.append(nextChar);
      else {
        String result = sb.toString();
        if (Util.isKeyword(result))
          tokens.add(new Token("keyword", result));
        else
          tokens.add(new Token("identifier", result));
        return nextChar;
      }
    }
    return 0;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("<tokens>\n");
    for (Token token : tokens) {
      sb.append(token.toString());
    }
    sb.append("</tokens>\n");
    return sb.toString();
  }
}