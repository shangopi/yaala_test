package org.initial_questions;

import java.util.Arrays;
import java.util.List;

public class Q1 {

    public static void main(String[] args) {
        List<Integer> number = Arrays.asList(2, 3, 4, 5);
        //one way
        double avg = (double) number.stream().reduce(0, Integer::sum) / number.size();
        System.out.println(avg);

        //another way
        Double average = number.stream().mapToInt(Integer::intValue).average().orElse(0d);
        System.out.println(average);
    }
}
