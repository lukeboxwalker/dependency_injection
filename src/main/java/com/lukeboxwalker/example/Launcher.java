package com.lukeboxwalker.example;

public final class Launcher {

    public static void main(final String... args) {
        System.out.println("Start...");
        final Controller controller = new Controller();
        controller.run();
    }
}
