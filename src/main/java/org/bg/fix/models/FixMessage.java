package org.bg.fix.models;

import lombok.*;

import java.util.List;

import static org.bg.fix.models.Constants.FIX_PROTOCOL;

@Getter
@Setter
@ToString
public abstract class FixMessage {

    private Header header;
    private FixMessage object;
    private String fixMsgStr;
    private int trailer;

    public static void validateCheckSum(String fixMessage, int trailer) throws RuntimeException {
        int calculatedChecksum = calculateCheckSum(fixMessage);
        if (trailer != calculatedChecksum) {
            throw new RuntimeException("Invalid checksum: expected = " + trailer + ", actual = " +
                    calculatedChecksum);
        }
    }

    public static int calculateCheckSum(String fixMessage) {
        int checksum = 0;
        int checksumtagIdx = fixMessage.lastIndexOf("10=");
        int len = checksumtagIdx > -1 ? checksumtagIdx + 1 : fixMessage.length();
        for (int i = 0; i < len; i++) {
            checksum += fixMessage.charAt(i);
        }
        return checksum % 256;
    }

    public abstract void setData(List<FixMessage.Field> fields) throws
            Exception;

    @AllArgsConstructor
    @Getter
    public static class Field {
        private int tag;
        private String value;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Header {
        private String protocol;
        private Integer msgLength;
        private String msgType;

        public void validate() throws RuntimeException {
            if (!this.protocol.equals(FIX_PROTOCOL)) {
                throw new RuntimeException("Unsupported protocol: " + this.protocol);
            }
            if (this.msgLength < 1) {
                throw new RuntimeException("Length must be greater than 0");
            }

            if (!List.of(NewOrder.MSG_TYPE).contains(this.msgType)) {
                throw new RuntimeException("Unsupported fix message type: " + this.msgType);
            }
        }
    }

}

