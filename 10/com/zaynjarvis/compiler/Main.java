package com.zaynjarvis.compiler;

import java.io.*;

public class Main {
  static public void main(String[] args) throws IOException {
    String fileLocation;
    if (args.length > 0) {
      fileLocation = args[0];
    } else {
      fileLocation = "./Square";
    }
    if (fileLocation.endsWith(".jack")) {
      Process process = new Process(new File(fileLocation));
      process.execute();
    } else {
      File[] files = new File(fileLocation).listFiles();
      for (File file : files) {
        if (file.getName().endsWith(".jack")) {
          Process process = new Process(file);
          process.execute();
        }
      }
    }
  }
}