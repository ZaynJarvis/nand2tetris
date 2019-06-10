package com.zaynjarvis.compiler.model;

public class Symbol {
  private String name;
  private String type;
  private String kind;
  private int index;

  public Symbol(String name, String type, String kind) {
    this.name = name;
    this.type = type;
    this.kind = kind.equals("var") ? "local" : kind;
  }

  public Symbol(Token name, Token type, Token kind) {
    this.name = name.getValue();
    this.type = type.getValue();
    this.kind = kind.getValue().equals("var") ? "local" : kind.getValue();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public boolean isClassScope() {
    if (this.kind.equals("field"))
      throw new Error("Field keyword should be changed to this keyword in runtime");
    return this.kind.equals("this") || this.kind.equals("static");
  }

  @Override
  public String toString() {
    return "Symbol [index=" + index + ", kind=" + kind + ", name=" + name + ", type=" + type + "]";
  }

}