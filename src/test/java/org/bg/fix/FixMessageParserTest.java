package org.bg.fix;

import org.bg.fix.models.FixMessage;
import org.bg.fix.models.NewOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FixMessageParserTest {

    @Test
    public void setData_validFields_setsAllFieldsCorrectly() throws Exception {
        String validMessage = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u0001109=ultrarichclient\u000176=jp\u00011=clientacc\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=21\u0001";

        NewOrder newOrder = (NewOrder) FixMessageParser.parseFixMessage(validMessage.getBytes(), true);
        Assertions.assertEquals("123", newOrder.getClOrdID());
        Assertions.assertEquals("ultrarichclient", newOrder.getClientID());
        Assertions.assertEquals("jp", newOrder.getExecBroker());
        Assertions.assertEquals("clientacc", newOrder.getAccount());
        Assertions.assertEquals(2, newOrder.getNoAllocs());
        Assertions.assertEquals(2, newOrder.getAllocations().size());
        Assertions.assertEquals("acc1", newOrder.getAllocations().get(0).getAllocAccount());
        Assertions.assertEquals(100.0, newOrder.getAllocations().get(0).getAllocShares());
        Assertions.assertEquals("acc2", newOrder.getAllocations().get(1).getAllocAccount());
        Assertions.assertEquals(200.0, newOrder.getAllocations().get(1).getAllocShares());
        Assertions.assertEquals("appl", newOrder.getSymbol());
        Assertions.assertEquals("1", newOrder.getSide());
        Assertions.assertEquals(300, newOrder.getOrderQty());
        Assertions.assertEquals(1.67, newOrder.getPrice());
        Assertions.assertEquals("1", newOrder.getOrderType());
    }

    @Test
    public void parseFixMessage_validMessage_parsesSuccessfully() throws Exception {
        String validMessage = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u0001109=ultrarichclient\u000176=jp\u00011=clientacc\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=21\u0001";
        FixMessage fixMessage = FixMessageParser.parseFixMessage(validMessage.getBytes(), true);
        Assertions.assertNotNull(fixMessage);
    }

    @Test
    public void parseFixMessage_invalidField_throwsException() {
        String invalidMessage = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u0001109=ultrarichclient\u000176=jp\u0001invalidField\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=21\u0001";
        Assertions.assertThrows(RuntimeException.class, () -> FixMessageParser.parseFixMessage(invalidMessage.getBytes(), true));
    }

    @Test
    public void parseFixMessage_unsupportedMsgType_throwsRuntimeException() {
        String unsupportedMsgTypeMessage = "8=FIX.4.2\u00019=142\u000135=Z\u000111=123\u0001109=ultrarichclient\u000176=jp\u00011=clientacc\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=21\u0001";
        Assertions.assertThrows(RuntimeException.class, () -> FixMessageParser.parseFixMessage(unsupportedMsgTypeMessage.getBytes(), true));
    }

    @Test
    public void parseFixMessage_missingTrailer_throwsException() {
        String missingTrailerMessage = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u0001109=ultrarichclient\u000176=jp\u00011=clientacc\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1";
        Assertions.assertThrows(Exception.class, () -> FixMessageParser.parseFixMessage(missingTrailerMessage.getBytes(), true));
    }

    @Test
    public void parseFixMessage_invalidChecksum_throwsRuntimeException() {
        String invalidChecksumMessage = "8=FIX.4.2\u00019=142\u000135=D\u000111=123\u0001109=ultrarichclient\u000176=jp\u00011=clientacc\u0001"
                + "78=2\u000179=acc1\u000180=100\u000179=acc2\u000180=200\u000155=appl\u000154=1\u000138=300\u0001"
                + "44=1.67\u000140=1\u000110=999\u0001";
        Assertions.assertThrows(RuntimeException.class, () -> {
            FixMessage fixMessage = FixMessageParser.parseFixMessage(invalidChecksumMessage.getBytes(), true);
        });
    }
}