package org.galibier.messaging.benchmark;

public class Message {
    private static final String DEFAULT_MESSAGE = "message";

    public static byte[] getDefault() {
        return DEFAULT_MESSAGE.getBytes();
    }
}
