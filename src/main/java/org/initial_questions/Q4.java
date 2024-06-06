package org.initial_questions;

import java.util.Scanner;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newFixedThreadPool;

class PrimeCalculation {
    int n; //indicates the input n
    int result; // stores result of computation

    public PrimeCalculation(int n) {
        this.n = n;
        this.result = PrimeCalculator.calculateNthPrime(n); //calling corresponding function to calculate nth prime
    }

    @Override
    public String toString() {
        return String.format("%d th prime is %d", this.n, this.result);
    }
}

public class Q4 {

    public static void main(String[] args) {

        ThreadPoolExecutor pool = (ThreadPoolExecutor) newFixedThreadPool(5); //creating pool of size 5
        CompletionService<PrimeCalculation> taskCompletionService = new ExecutorCompletionService<>(pool);

        while (true) {
            // Read the user input
            Scanner scanner = new Scanner(System.in);
            System.out.print("Press 0 to exit. \n Enter the number of primes : ");
            int n = scanner.nextInt();
            if (n == 0) break;

            if (pool.getActiveCount() >= pool.getMaximumPoolSize()) {
                System.out.println("No free thread. Try again");
                continue;
            }

            taskCompletionService.submit(() -> new PrimeCalculation(n));

            System.out.printf("Prime number calculation for %d submitted !!!%n", n);

            try {
                Future<PrimeCalculation> result;
                do {
                    result = taskCompletionService.poll(); //for unblock execution, poll() is used instead of take()
                    if (result != null) { //poll() method returns null if no jobs completed
                        System.out.println(result.get());
                    }
                } while (result != null); //need to check other jobs too if previous job is completed
            } catch (InterruptedException e) {
                System.out.println("Interrupted Exception has been thrown");
            } catch (ExecutionException e) {
                System.out.println("ExecutionException has been thrown");
            }
        }
        pool.shutdown();
    }
}
