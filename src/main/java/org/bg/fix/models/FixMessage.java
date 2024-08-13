package org.bg.fix.models;

import lombok.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class FixMessage {

    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Header {
        private final String FIX_PROTOCOL = "FIX.4.4";
        private String protocol;
        private Integer msgLength;
        private FixMessageType.FIX_MSG_TYPE msgType;

        public void validate() throws RuntimeException {
            if (!this.protocol.equals(FIX_PROTOCOL)) {
                throw new RuntimeException("Unsupported protocol: " + this.protocol);
            }
            if (this.msgLength < 1) {
                throw new RuntimeException("Length must be greater than 0");
            }

            //TODO: revisit this
            if (!Arrays.asList(FixMessageType.FIX_MSG_TYPE.values()).contains(this.msgType)) {
                throw new RuntimeException("Unsupported fix message type: " + this.msgType);
            }
        }
    }

    private Header header;
    private Map<Integer, String> body;
    private String trailer;

    public FixMessage() {
        this.header = new Header();
        this.body = new HashMap<>();
        this.trailer = "";
    }

    public FixMessage(Map<Integer, String> tags) {
        if (tags == null) throw new RuntimeException("tags is null");
        new FixMessage();
        this.header = new Header();
        this.body = new HashMap<>();
        this.trailer = "";
        for (Map.Entry<Integer, String> entry : tags.entrySet()) {
            switch (entry.getKey()) {
                case 8:
                    this.header.protocol = entry.getValue();
                    break;
                case 9:
                    this.header.msgLength = Integer.valueOf(entry.getValue());
                    break;
                case 35:
                    this.header.msgType = FixMessageType.FIX_MSG_TYPE.valueOf(entry.getValue());
                    break;
                case 10:
                    this.trailer = entry.getValue();
                    break;
                default:
                    this.body.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addField(Integer tag, String value) {
        body.put(tag, value);
    }

    public int calculateCheckSum() {
        int checksum = 0;
        for (Map.Entry<Integer, String> entry : body.entrySet()) {
            checksum += entry.getKey() + entry.getValue().length();
        }
        return checksum % 256;
    }

    //TODO: revisit this
    public static void validateSyntax(String fixMessage) throws RuntimeException {
        throw new RuntimeException("Invalid state: Syntax invalid");
    }

    public void validateBody() throws RuntimeException {
        throw new RuntimeException("Invalid body");
    }

    public void validate() throws RuntimeException {
        this.getHeader().validate();
        this.validateBody();
        if (!this.trailer.equals(Integer.toString(this.calculateCheckSum()))) {
            throw new RuntimeException("Invalid trailer: " + this.trailer);
        }
    }
}
