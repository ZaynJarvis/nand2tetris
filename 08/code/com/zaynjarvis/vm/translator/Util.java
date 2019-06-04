package com.zaynjarvis.vm.translator;

import java.util.*;

class Util {
  static Map<String, Integer> addressMap = new HashMap<>();
  static Map<String, Integer> functionMap = new HashMap<>();
  static Map<String, String> opMap = new HashMap<>();
  static int counter = 0;
  static String className = "global";

  static {
    addressMap.put("SP", 0);
    addressMap.put("local", 1);
    addressMap.put("argument", 2);
    addressMap.put("this", 3);
    addressMap.put("that", 4);
    addressMap.put("temp", 5);
    addressMap.put("static", 16);

    opMap.put("add", "+");
    opMap.put("sub", "-");
    opMap.put("and", "&");
    opMap.put("or", "|");
    opMap.put("not", "!");
    opMap.put("neg", "-");
  }

  static String getNewLabel() {
    return "LABEL." + Integer.toString(counter++);
  }

  static String getClassName() {
    return Util.className;
  }

  static void setClassName(String functionName) {
    Util.className = functionName.indexOf(".") != -1 ? functionName.substring(0, functionName.indexOf("."))
        : functionName;
  }

  static String getFunctionReturn(String functionName) {
    int count = functionMap.getOrDefault(functionName, 0);

    functionMap.put(functionName, ++count);
    return String.format("%s$RETURN.%d", functionName, count);
  }
}
