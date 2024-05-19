package com.javasl;

public class Main {
    public static void main(String[] args) {
        Script script = new Script();
        try {
            script.compileFromFile("scripts/TestS1.jsl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (script.isReady()) {
            script.run();
        } else {
            System.out.println("Script is not ready");
        }
    }
}