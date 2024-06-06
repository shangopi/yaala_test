package org.initial_questions;

public class PrimeCalculator {

    private PrimeCalculator() {
    }

    /**
     * Function to check whether the given number is prime or not
     *
     * @param n -> the number needed to be checked
     * @return boolean value whether given number is prime or not
     */
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Function to calculate n th prime number
     *
     * @param n -> nth number
     * @return nth prime value
     */
    public static int calculateNthPrime(int n) {
        int count = 0;
        int i = 2;
        while (count < n) {
            if (isPrime(i)) count += 1;
            i++;
        }
        try {
            Thread.sleep(20000); // adding 20 seconds delay to simulate heavy processing
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted Exception has been thrown", e);
        }
        return i - 1;
    }
}
