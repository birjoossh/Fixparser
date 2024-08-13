import org.bg.fix.Parser;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void test1(){
        String validMessage = "8=FIX.4.4\u00019=142\u000135=W\u000134=0\u000149=justtech\u000152=20180206-21:43:36.000\u0001"
                + "56=user\u0001262=TEST\u000155=EURUSD\u0001268=2\u0001269=0\u0001270=1.31678\u0001271=100000.0\u0001269=1\u0001"
                + "270=1.31667\u0001271=100000.0\u000110=057\u0001";
        System.out.println("checksum = " + Parser.validateCheckSum(validMessage));
    }

}
