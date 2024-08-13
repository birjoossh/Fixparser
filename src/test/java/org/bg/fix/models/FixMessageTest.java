package org.bg.fix.models;

import org.bg.fix.FixMessageParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FixMessageTest {

    @Test
    public void checksum_validates_successfully() {

        String validMessage = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u000176=jp\u00011=ultrarichclient\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=151";
        Assertions.assertEquals(151, FixMessage.calculateCheckSum(validMessage));
    }

    @Test
    public void incorrect_checksum_validation_fails() {
        String validMessage = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u000176=jp\u00011=ultrarichclient\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=153";
        Assertions.assertThrows(RuntimeException.class, () -> {
            FixMessage fixMessage = FixMessageParser.parseFixMessage(validMessage.getBytes(), false);
            FixMessage.validateCheckSum(validMessage, 153);
        });
    }

    @Test
    public void header_validates_successfully() {
        String validMessage = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u000176=jp\u00011=ultrarichclient\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=151";

        Assertions.assertDoesNotThrow(() -> {
            FixMessage fixMessage = FixMessageParser.parseFixMessage(validMessage.getBytes(), false);
            fixMessage.getHeader().validate();
        });
    }

    @Test
    public void invalid_header_validation_fails() {
        String validMessage = "8=FIX.4.4\u00019=142\u000135=D\u000111=123\u000176=jp\u00011=ultrarichclient\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=151";

        Assertions.assertThrows(RuntimeException.class, () -> {
            FixMessage fixMessage = FixMessageParser.parseFixMessage(validMessage.getBytes(), false);
            fixMessage.getHeader().validate();
        });
    }
}