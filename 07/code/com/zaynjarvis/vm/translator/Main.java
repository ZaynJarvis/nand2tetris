package com.zaynjarvis.vm.translator;

import java.io.IOException;

public class Main {
  static public void main(String[] args) throws IOException {
    if (args.length < 1)
      throw new Error("Please provide file name");
    Process process = new Process(args[0]);
    process.execute();
  }
}