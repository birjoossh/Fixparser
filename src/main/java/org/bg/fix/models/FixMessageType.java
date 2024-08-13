package org.bg.fix.models;

public class FixMessageType {
    public enum FIX_MSG_TYPE {
        HEARTBEAT(0), // Heartbeat
        TEST_REQUEST(1), // Test Request
        RESEND_REQUEST(2), // Resend Request
        REJECT(3), // Reject
        SEQUENCE_RESET(4), // Sequence Reset
        LOGOUT(5), // Logout
        EXECUTION_REPORT(8), // Execution Report
        ORDER_CANCEL_REJECT(9), // Order Cancel Reject
        LOGON(10), // Logon
        NEWS(11), // News
        SECURITY_DEFINITION_REQUEST(12), // Security Definition Request
        ORDER_SINGLE(13), // Order Single
        SECURITY_DEFINITION(14), // Security Definition
        SECURITY_STATUS_REQUEST(15), // Security Status Request
        SECURITY_STATUS(16), // Security Status
        ORDER_CANCEL_REQUEST(17), // Order Cancel Request
        ORDER_CANCEL_REPLACE_REQUEST(18), // Order Cancel Replace Request
        ORDER_STATUS_REQUEST(19), // Order Status Request
        DONT_KNOW_TRADE(20), // Don't Know Trade (Inbound Drop Copy only)
        QUOTE_REQUEST(21), // Quote Request
        MARKET_DATA_REQUEST(22), // Market Data Request
        MARKET_DATA_SNAPSHOT_FULL_REFRESH(23), // Market Data Snapshot Full Refresh
        MARKET_DATA_INCREMENTAL_REFRESH(24), // Market Data Incremental Refresh
        MARKET_DATA_REQUEST_REJECT(25), // Market Data Request Reject
        TRADE_CAPTURE_REPORT_REQUEST(26), // Trade Capture Report Request
        TRADE_CAPTURE_REPORT(27), // Trade Capture Report
        TRADE_CAPTURE_REPORT_REQUEST_ACK(28); // Trade Capture Report Request Ack

        private final int code;

        FIX_MSG_TYPE(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
