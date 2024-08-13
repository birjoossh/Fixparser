package org.bg.fix.models;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

//<href url="https://www.onixs.biz/fix-dictionary/4.2/msgType_D_68.html">Fix New Order Dictionary</href>
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewOrder extends FixMessage {
    public static final String MSG_TYPE = "D";
    private String clOrdID; //tag 11
    private String clientID; //tag 109
    private String execBroker; //tag 76
    private String account; //tag 1
    private Integer noAllocs; //tag 78
    private List<Allocation> allocations;
    private String symbol; //tag 55
    private String side; //tag 54
    private Integer orderQty; //tag 38
    private Double price; //tag 44
    private String orderType; //tag 40

    @Override
    public void setData(List<FixMessage.Field> fields, int bodyStart, int bodyEnd) throws Exception {
        for (int i = bodyStart; i < bodyEnd; i++) {
            Field field = fields.get(i);
            int tag = field.getTag();
            switch (tag) {
                case 11 -> this.clOrdID = field.getValue();
                case 109 -> this.clientID = field.getValue();
                case 76 -> this.execBroker = field.getValue();
                case 1 -> this.account = field.getValue();
                case 78 -> this.noAllocs = Integer.parseInt(field.getValue());
                case 79 -> addAllocationAccount(field.getValue());
                case 80 -> addAllocationShares(field.getValue());
                case 55 -> this.symbol = field.getValue();
                case 54 -> this.side = field.getValue();
                case 38 -> this.orderQty = Integer.parseInt(field.getValue());
                case 44 -> this.price = Double.parseDouble(field.getValue());
                case 40 -> this.orderType = field.getValue();
                default ->
                        throw new RuntimeException("Unknown body tag " + field.getTag() + " with value " + field.getValue());
            }
        }
        if (allocations.size() != this.noAllocs) {
            throw new RuntimeException("Expected allocations = " + this.noAllocs + ", but allocations found = " + allocations.size());
        }
    }

    private void addAllocationAccount(String account) {
        Allocation allocation = getLastAllocation();
        if (allocation.getAllocAccount() == null) {
            allocation.setAllocAccount(account);
        } else {
            allocations.add(new Allocation(account, null));
        }
    }

    private void addAllocationShares(String shares) {
        Allocation allocation = getLastAllocation();
        if (allocation.getAllocShares() == null) {
            allocation.setAllocShares(Double.parseDouble(shares));
        } else {
            allocations.add(new Allocation(null, Double.parseDouble(shares)));
        }
    }

    private Allocation getLastAllocation() {
        if (allocations == null) {
            allocations = new ArrayList<>();
        }
        if (allocations.isEmpty() || allocations.get(allocations.size() - 1).isEmpty()) {
            allocations.add(new Allocation());
        }
        return allocations.get(allocations.size() - 1);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class Allocation {
        private String allocAccount; //tag 79
        private Double allocShares; //tag 80

        public Boolean isEmpty() {
            return allocAccount == null && allocShares == null;
        }
    }

}



