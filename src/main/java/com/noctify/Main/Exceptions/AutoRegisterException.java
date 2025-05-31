package com.noctify.Main.Exceptions;

public class AutoRegisterException extends Exception {
    public AutoRegisterException(String className, Throwable cause) {
        super("Failed to auto-register: " + className + " - " + cause.getMessage(), cause);
    }
}