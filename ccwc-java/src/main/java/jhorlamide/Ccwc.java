package jhorlamide;

import java.util.concurrent.Callable;

public class Ccwc implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "Hello, World!";
    }
}
