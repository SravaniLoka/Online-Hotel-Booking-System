
package com.booking;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class ConsoleScanner {

    private final Terminal terminal;
    private final LineReader lineReader;
    private boolean pendingLineAfterToken = false;

    public ConsoleScanner() {
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

        } catch (java.io.IOException e) {
            throw new RuntimeException(
                    "Unable to initialize console input", e
            );
        }
    }

    public String nextLine() {
        if (pendingLineAfterToken) {
            pendingLineAfterToken = false;
            return "";
        }

        return lineReader.readLine();
    }

    public int nextInt() {
        pendingLineAfterToken = false;

        int value = Integer.parseInt(
                lineReader.readLine().trim()
        );

        pendingLineAfterToken = true;
        return value;
    }

    public long nextLong() {
        pendingLineAfterToken = false;

        long value = Long.parseLong(
                lineReader.readLine().trim()
        );

        pendingLineAfterToken = true;
        return value;
    }

    public String nextPassword() {
        return lineReader.readLine('*');
    }

    public void close() {
        try {
            terminal.close();
        } catch (Exception ignored) {
            // Ignore console close errors.
        }
    }
}
