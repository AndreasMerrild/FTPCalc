package Fit;

import java.time.Instant;

/**
 * One recorded sample from a FIT activity file.
 */
public record FitRecord(
        Instant timestamp,
        Integer power,
        Integer heartRate,
        Double speed,
        Integer cadence) {
}
