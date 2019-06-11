package com.zaynjarvis.compiler;

import java.io.*;
import java.util.*;

class Process {
  File file;
  String fileLocation;

  Process(String fileLocation, File file) {
    this.file = file;
    this.fileLocation = fileLocation + "/vm/";
  }

  Process(File file) {
    this.file = file;
  }

  void execute() throws IOException {
    try (Scanner sc = new Scanner(new BufferedReader(new FileReader(file)));
        BufferedWriter bw = new BufferedWriter(
            new FileWriter(new File(fileLocation + file.getName().replace(".jack", ".vm"))));) {
      sc.useDelimiter("");
      Lexer lexer = new Lexer(sc);
      Parser parser = new Parser(lexer.getTokens(), bw);
      parser.parse();
    }
  }
}