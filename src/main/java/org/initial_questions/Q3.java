package org.initial_questions;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Q3 {
    public static void main(String[] args) {
        // Read the user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of primes : ");
        int n = scanner.nextInt();
        scanner.close();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> futureResult = executorService.submit(() -> PrimeCalculator.calculateNthPrime(n));

        while (!futureResult.isDone()) {
            System.out.println("Task is still in progress...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception has been thrown");
            }
        }

        int resultFromFuture = 0;
        try {
            resultFromFuture = futureResult.get();
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception has been thrown");
        } catch (ExecutionException e) {
            System.out.println("ExecutionException has been thrown");
        }
        System.out.printf("%d th prime is %d%n", n, resultFromFuture);

        executorService.shutdown();
    }
}
