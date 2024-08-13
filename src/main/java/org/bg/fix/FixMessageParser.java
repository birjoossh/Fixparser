package org.bg.fix;

import org.bg.fix.models.FixMessage;
import org.bg.fix.models.NewOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.bg.fix.models.Constants.SOH;

public class FixMessageParser {

    private final ExecutorService executorService;

    public FixMessageParser() {
        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        this.executorService = Executors.newFixedThreadPool(numberOfProcessors);
    }

    public static FixMessage parseFixMessage(byte[] fixMessageStringArray, Boolean validateMessage) throws Exception {
        String fixMessageString = new String(fixMessageStringArray);
        String[] keyValuePairs = fixMessageString.split(String.valueOf(SOH));

        List<FixMessage.Field> fields = new ArrayList<>();

        FixMessage.Header header = new FixMessage.Header();
        int trailer = -1;
        int bodyStart = -1;
        int bodyEnd = -1;
        for (int i = 0; i < keyValuePairs.length; i++) {
            String[] parts = keyValuePairs[i].split("=", 2);
            if (parts.length != 2) {
                throw new RuntimeException("Invalid FIX field: " + keyValuePairs[i]);
            }
            int tag = Integer.parseInt(parts[0]);
            String value = parts[1];
            fields.add(new FixMessage.Field(tag, value));

            switch (tag) {
                case 8 -> header.setProtocol(value);
                case 9 -> header.setMsgLength(Integer.valueOf(value));
                case 35 -> header.setMsgType(value);
                case 10 -> {
                    trailer = Integer.parseInt(value);
                    if (validateMessage) FixMessage.validateCheckSum(fixMessageString, trailer);
                    bodyEnd = i;
                }
                default -> bodyStart = bodyStart == -1 ? i : bodyStart; //initialize bodyStart if not already set
            }
        }
        if (validateMessage) header.validate();
        return parseBody(header, trailer, fields, bodyStart, bodyEnd);
    }

    public static FixMessage parseBody(FixMessage.Header header, int trailer, List<FixMessage.Field> fields, int bodyStart, int bodyEnd) throws Exception {
        FixMessage fixMessage;
        if ("D".equals(header.getMsgType())) {
            fixMessage = new NewOrder();
            fixMessage.setData(fields, bodyStart, bodyEnd);
        } else {
            throw new RuntimeException("Unsupported fix message type: " + header.getMsgType());
        }
        fixMessage.setTrailer(trailer);
        fixMessage.setHeader(header);
        return fixMessage;
    }

    public CompletableFuture<FixMessage> parseFixMessageAsynchronous(byte[] fixMessageStringArray, Boolean validateMessage) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return parseFixMessage(fixMessageStringArray, validateMessage);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }, executorService);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

