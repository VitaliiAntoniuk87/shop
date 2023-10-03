package com.example.petproject.processor;

public class TestProcessor extends BatchProcessor {
    @Override
    public void run() {
        System.out.println("test processor running");
    }
}
