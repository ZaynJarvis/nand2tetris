package com.zaynjarvis.vm.translator;

import java.util.*;
import java.io.*;

class Process {
  Scanner sc;
  BufferedWriter bw;

  Process(Scanner sc, BufferedWriter bw) throws IOException {
    this.sc = sc;
    this.bw = bw;
  }

  void execute() throws IOException {
    while (sc.hasNextLine()) {
      String newLine = sc.nextLine().trim();
      if (newLine.startsWith("/*"))
        while (sc.hasNextLine() && !sc.nextLine().contains("*/"))
          ;
      String[] srcArr = newLine.replace("\r", "").replace("\n", "").split("//");
      String src = srcArr.length > 0 ? srcArr[0].trim() : ""; // Solving the "//" issue
      if (src.isEmpty())
        continue;

      Token token = new Token(src);
      bw.write(ParserController.parse(token));
    }
  }
}