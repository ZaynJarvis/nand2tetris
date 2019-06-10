package com.zaynjarvis.compiler;

import java.io.BufferedWriter;
import java.io.IOException;

class VMWriter {
  BufferedWriter bw;

  VMWriter(BufferedWriter bw) {
    this.bw = bw;
  }

  void writePush(String segment, int index) throws IOException {
    bw.write(String.format("push %s %d", segment, index));
    bw.newLine();
  }

  void writePop(String segment, int index) throws IOException {
    bw.write(String.format("pop %s %d", segment, index));
    bw.newLine();
  }

  void writeALU(String command) throws IOException {
    bw.write(command);
    bw.newLine();
  }

  void writeLabel(String label) throws IOException {
    bw.write(String.format("label %s", label));
    bw.newLine();
  }

  void writeGoto(String label) throws IOException {
    bw.write(String.format("goto %s", label));
    bw.newLine();
  }

  void writeIf(String label) throws IOException {
    bw.write(String.format("if-goto %s", label));
    bw.newLine();
  }

  void writeCall(String name, int agn) throws IOException {
    bw.write(String.format("call %s %d", name, agn));
    bw.newLine();
  }

  void writeFunction(String name, int agn) throws IOException {
    bw.write(String.format("function %s %d", name, agn));
    bw.newLine();
  }

  void writeReturn() throws IOException {
    bw.write("return");
    bw.newLine();
  }

  void close() throws IOException {
    bw.close();
  }
}