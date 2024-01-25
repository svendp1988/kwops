package pxl.kwops.test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;
import java.util.UUID;

public class RandomExtensions {

    private static final Random random = new Random();
    private static final int exclusiveMaximum = Integer.MAX_VALUE;
    private static final int exclusiveMinimum = Integer.MIN_VALUE;

    /**
     * Returns a positive integer.
     */
    public static int nextPositive() {
        return nextPositive(exclusiveMaximum);
    }

    public static int nextPositive(int max) {
        return random.nextInt(max - 1) + 1;
    }

    /**
     * Returns a zero or negative integer.
     *
     */
    public static int nextZeroOrNegative() {
        return -1 * random.nextInt(-exclusiveMinimum - 1);
    }

    /**
     * Returns true or false.
     */
    public static boolean nextBool() {
        return random.nextBoolean();
    }

    /**
     * Returns a randomly generated string.
     */
    public static String nextString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Returns a random date and time in the future.
     */
    public static LocalDate nextDateTimeInFuture() {
        return LocalDate.now().plus(Period.ofDays(random.nextInt(3650) + 1));
    }

    /**
     * Returns a random date and time in the past.
     */
    public static LocalDate nextDateTimeInPast() {
        return LocalDate.now().minus(Period.ofDays(random.nextInt(3650) + 1));
    }

    /**
     * Returns a random date and time after the specified start date and time.
     */
    public static LocalDate nextDateTimeAfterStartDate(LocalDate startDate) {
        long hoursToAdd = random.nextInt(87600) + 1; // 87600 hours is roughly 10 years
        return startDate.plusDays(hoursToAdd / 24);
    }
}
