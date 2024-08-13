package org.bg.fix.models;

public enum FieldEnum {
    clOrdID(11),
    clientID(109),
    execBroker(76),
    account(1),
    noAllocs(78),
    allocAccount(79),
    allocShares(80),
    symbol(55),
    side(54),
    orderQty(38),
    price(44),
    orderType(40);

    private final int code;

    FieldEnum(int code) {
        this.code = code;
    }

    public static FieldEnum fromCode(int code) {
        for (FieldEnum type : FieldEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }
}

