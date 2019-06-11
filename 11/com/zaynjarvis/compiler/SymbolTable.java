package com.zaynjarvis.compiler;

import java.util.*;
import com.zaynjarvis.compiler.model.*;

class SymbolTable {
  private HashMap<String, Symbol> classScopeMap;
  private HashMap<String, Symbol> subroutineScopeMap;
  private HashMap<String, Integer> classIndexMap;
  private HashMap<String, Integer> indexMap;

  SymbolTable() {
    this.classScopeMap = new HashMap<>();
    this.classIndexMap = new HashMap<>();
    this.indexMap = new HashMap<>(this.classIndexMap);
  }

  boolean contains(String name) {
    return classScopeMap.keySet().contains(name) || subroutineScopeMap.keySet().contains(name);
  }

  void startSubroutine() {
    this.subroutineScopeMap = new HashMap<>();
    this.indexMap = new HashMap<>(this.classIndexMap);
  }

  void define(Symbol symbol) {
    String name = symbol.getName();
    String kind = symbol.getKind();
    kind = "field".equals(kind) ? "this" : kind;
    symbol.setKind(kind);
    int newIndex = indexMap.getOrDefault(kind, -1) + 1;
    symbol.setIndex(newIndex);
    indexMap.put(kind, newIndex);
    if (symbol.isClassScope()) {
      classScopeMap.put(name, symbol);
      classIndexMap.put(kind, newIndex);
    } else
      subroutineScopeMap.put(name, symbol);
  }

  int varCount(String kind) {
    kind = kind == "field" ? "this" : kind;
    return indexMap.getOrDefault(kind, -1) + 1;
  }

  Symbol getSymbol(String name) {
    Symbol s;
    if (!contains(name))
      throw new Error("The Symbol matches" + name + " cannot be found");
    s = classScopeMap.getOrDefault(name, null);
    if (s == null)
      s = subroutineScopeMap.get(name);
    return s;
  }
}