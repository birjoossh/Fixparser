package org.bg.fix.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class NewOrderTest {
    @Test
    public void setData_validFields_setsAllFieldsCorrectly() throws Exception {
        NewOrder newOrder = new NewOrder();
        List<FixMessage.Field> fields = Arrays.asList(new FixMessage.Field(11, "123"),
                new FixMessage.Field(109, "client1"), new FixMessage.Field(76, "broker1"),
                new FixMessage.Field(1, "account1"), new FixMessage.Field(78, "2"),
                new FixMessage.Field(79, "alloc1"), new FixMessage.Field(80, "100.0"),
                new FixMessage.Field(79, "alloc2"), new FixMessage.Field(80, "200.0"),
                new FixMessage.Field(55, "APPL"), new FixMessage.Field(54, "1"),
                new FixMessage.Field(38, "300"), new FixMessage.Field(44, "1.67"),
                new FixMessage.Field(40, "1"));

        newOrder.setData(fields);//, 0, fields.size());

        Assertions.assertEquals("123", newOrder.getClOrdID());
        Assertions.assertEquals("client1", newOrder.getClientID());
        Assertions.assertEquals("broker1", newOrder.getExecBroker());
        Assertions.assertEquals("account1", newOrder.getAccount());
        Assertions.assertEquals(2, newOrder.getNoAllocs());
        Assertions.assertEquals(2, newOrder.getAllocations().size());
        Assertions.assertEquals("alloc1", newOrder.getAllocations().get(0).getAllocAccount());
        Assertions.assertEquals(100.0, newOrder.getAllocations().get(0).getAllocShares());
        Assertions.assertEquals("alloc2", newOrder.getAllocations().get(1).getAllocAccount());
        Assertions.assertEquals(200.0, newOrder.getAllocations().get(1).getAllocShares());
        Assertions.assertEquals("APPL", newOrder.getSymbol());
        Assertions.assertEquals("1", newOrder.getSide());
        Assertions.assertEquals(300, newOrder.getOrderQty());
        Assertions.assertEquals(1.67, newOrder.getPrice());
        Assertions.assertEquals("1", newOrder.getOrderType());
    }

    @Test
    public void setData_validFields_incorrect_allocations_count_throwsException() {
        NewOrder newOrder = new NewOrder();
        List<FixMessage.Field> fields = Arrays.asList(new FixMessage.Field(11, "123"),
                new FixMessage.Field(109, "client1"), new FixMessage.Field(76, "broker1"),
                new FixMessage.Field(1, "account1"), new FixMessage.Field(78, "3"),
                new FixMessage.Field(79, "alloc1"), new FixMessage.Field(80, "100.0"),
                new FixMessage.Field(79, "alloc2"), new FixMessage.Field(80, "200.0"),
                new FixMessage.Field(55, "APPL"), new FixMessage.Field(54, "1"),
                new FixMessage.Field(38, "300"), new FixMessage.Field(44, "1.67"),
                new FixMessage.Field(40, "1"));
        Assertions.assertThrows(RuntimeException.class, () -> newOrder.setData(fields));//, 0, fields.size()));
    }

    @Test
    public void setData_invalidField_throwsException() {
        NewOrder newOrder = new NewOrder();
        List<FixMessage.Field> fields = Arrays.asList(new FixMessage.Field(1000, "123"),
                new FixMessage.Field(109, "client1"), new FixMessage.Field(76, "broker1"),
                new FixMessage.Field(1, "account1"), new FixMessage.Field(78, "3"),
                new FixMessage.Field(79, "alloc1"), new FixMessage.Field(80, "100.0"),
                new FixMessage.Field(79, "alloc2"), new FixMessage.Field(80, "200.0"),
                new FixMessage.Field(55, "APPL"), new FixMessage.Field(54, "1"),
                new FixMessage.Field(38, "300"), new FixMessage.Field(44, "1.67"),
                new FixMessage.Field(40, "1"));

        Assertions.assertThrows(RuntimeException.class, () -> newOrder.setData(fields));//, 0, fields.size()));
    }

    @Test
    public void setData_missingValue_throwsException() {
        NewOrder newOrder = new NewOrder();
        List<FixMessage.Field> fields = Arrays.asList(new FixMessage.Field(11, ""),
                new FixMessage.Field(109, "client1"), new FixMessage.Field(76, "broker1"),
                new FixMessage.Field(1, "account1"), new FixMessage.Field(78, "3"),
                new FixMessage.Field(79, "alloc1"), new FixMessage.Field(80, "100.0"),
                new FixMessage.Field(79, "alloc2"), new FixMessage.Field(80, "200.0"),
                new FixMessage.Field(55, "APPL"), new FixMessage.Field(54, "1"),
                new FixMessage.Field(38, "300"), new FixMessage.Field(44, "1.67"),
                new FixMessage.Field(40, "1"));

        Assertions.assertThrows(Exception.class, () -> newOrder.setData(fields));//, 0, fields.size()));
    }
}
