package com.zaynjarvis.vm.translator;

import java.util.*;

final class Util {
  static Map<String, Integer> addressMap = new HashMap<>();
  static Map<String, String> opMap = new HashMap<>();
  static int counter = 0;
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
    return "LABEL. " + Integer.toString(counter++);
  }
}
