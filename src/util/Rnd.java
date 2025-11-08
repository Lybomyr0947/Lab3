package util;

import java.util.concurrent.ThreadLocalRandom;

public class Rnd {
    public static boolean chance(double p) { // p у [0..1]
        return ThreadLocalRandom.current().nextDouble() < p;
    }
    public static int nextInt(int bound) { return ThreadLocalRandom.current().nextInt(bound); }
}
