package Test;

import Fit.FitActivity;
import Fit.FitRecord;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class fitActivityTest {
    @Test
    void testAverageSpeedIsConvertedToKilometersPerHour() {
        FitActivity activity = new FitActivity(
                null,
                Instant.EPOCH,
                Instant.EPOCH.plusSeconds(1),
                List.of(
                        new FitRecord(Instant.EPOCH, null, null, 10.0, null),
                        new FitRecord(Instant.EPOCH.plusSeconds(1), null, null, 20.0, null)));

        assertEquals(54.0, activity.averageSpeed());
    }
}
