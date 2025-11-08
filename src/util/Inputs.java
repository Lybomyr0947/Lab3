package util;

import java.util.Scanner;

public class Inputs {
    public static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            System.out.print("Введіть число: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public static int readIndex(Scanner sc, String prompt, int size) {
        int idx;
        while (true) {
            idx = readInt(sc, prompt);
            if (idx >= 0 && idx < size) return idx;
            System.out.println("Індекс поза межами [0.." + (size - 1) + "]");
        }
    }
}
