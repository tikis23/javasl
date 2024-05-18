package com.javasl;

public class Main {
    public static void main(String[] args) {
        Script script = new Script();
        try {
            script.compileFromFile("scripts/testS1.jsl");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (script.isReady()) {
            System.out.println("Script is ready!");
        } else {
            System.out.println("Script is not ready!");
        }
    }
}