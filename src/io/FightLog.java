package io;

import java.util.ArrayList;
import java.util.List;

public class FightLog {
    private final List<String> lines = new ArrayList<>();

    public void header(String fmt, Object... args) { lines.add("=== " + String.format(fmt, args) + " ==="); }
    public void line(String fmt, Object... args)   { lines.add(String.format(fmt, args)); }
    public void footer(String fmt, Object... args) { lines.add("=== " + String.format(fmt, args) + " ==="); }

    public List<String> getLines() { return lines; }

    public boolean isEmpty() { return lines.isEmpty(); }

    public void flushToConsole() {
        for (String s : lines) System.out.println(s);
    }
}
