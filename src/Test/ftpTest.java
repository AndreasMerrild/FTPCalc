package Test;

import Ftp.ftp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ftpTest {
    private ftp calculator;

    @BeforeEach
    public void setUp() {
        String name = "hello";
        double weight = 0.00;
        calculator = new ftp(name, weight);
    }

    @Test
    void testFtpValues() {
        assertEquals("hello", calculator.getUser());
        assertEquals(0.00, calculator.getWeight());
        assertEquals(95.0, calculator.ftpCalc(100.0));
    }

}
