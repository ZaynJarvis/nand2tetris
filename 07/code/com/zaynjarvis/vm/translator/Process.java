package com.zaynjarvis.vm.translator;

import java.util.*;
import java.io.*;

class Process {
  String fileName;
  Scanner sc;
  BufferedWriter bw;

  Process(String srcFileName) throws IOException {
    fileName = srcFileName;
    sc = new Scanner(new FileReader(new File(fileName)));
    bw = new BufferedWriter(new FileWriter(new File(fileName.replace(".vm", ".asm"))));
  }

  void execute() throws IOException {
    while (sc.hasNextLine()) {
      String newLine = sc.nextLine().trim();
      if (newLine.startsWith("/*"))
        while (sc.hasNextLine() && !sc.nextLine().contains("*/"))
          ;
      String src = newLine.replace("\r", "").replace("\n", "").split("//")[0].trim();
      if (src.isEmpty())
        continue;

      Token token = new Token(src);
      bw.write(ParserController.parse(token));
    }
    bw.write(ParserController.end());
    sc.close();
    bw.close();
  }
}