package org.initial_questions;

import java.util.Arrays;
import java.util.List;

public class Q2 {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("exam", "Hexe", "Excel", "Exa", "Exact", "Test", "Executable", "Extra");
        List<String> outputList = names.stream().filter(str -> str.toUpperCase().contains("EXE")).toList();
        System.out.println(outputList);
    }
}
