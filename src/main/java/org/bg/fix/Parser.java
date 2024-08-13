package org.bg.fix;

import org.bg.fix.models.FixMessage;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    private static final char SOH = '\u0001';

    public FixMessage parseFixMessage(String fixMessageString) throws Exception {

        String[] fields = fixMessageString.split(String.valueOf(SOH));
        Map<Integer, String> tags = new HashMap<Integer, String>();

        for (String field : fields) {
            String[] parts = field.split("=", 2);
            if (parts.length != 2) {
                throw new Exception("Invalid FIX field: " + field);
            }
            int tag = Integer.parseInt(parts[0]);
            String value = parts[1];
            tags.put(tag, value);
        }
        FixMessage fixMessage = new FixMessage(tags);
        fixMessage.validate();
        return fixMessage;
    }

}
