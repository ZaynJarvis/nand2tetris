package com.zaynjarvis.vm.translator;

import java.io.*;
import java.util.Scanner;

public class Main {
  static public void main(String[] args) throws IOException {
    Scanner sc;
    BufferedWriter bw;
    String locator;

    if (args.length > 0)
      locator = args[0];
    else
      locator = "./FunctionCalls/StaticsTest";

    String[] temp = locator.split("/");
    String fileName = temp[temp.length - 1];

    if (locator.endsWith(".vm")) {
      sc = new Scanner(new FileReader(new File(locator)));
      bw = new BufferedWriter(new FileWriter(new File(locator.replace(".vm", ".asm"))));
      bw.write(ParserController.start());
      Process process = new Process(sc, bw);
      process.execute();
      sc.close();
    } else {
      File[] files = new File(locator).listFiles();
      bw = new BufferedWriter(new FileWriter(new File(locator + "/" + fileName + ".asm")));
      bw.write(ParserController.start());
      for (File file : files) {
        if (file.getName().endsWith(".vm")) {
          sc = new Scanner(new FileReader(file));
          Process process = new Process(sc, bw);
          process.execute();
          sc.close();
        }
      }
    }
    bw.write(ParserController.end());
    bw.close();
  }
}