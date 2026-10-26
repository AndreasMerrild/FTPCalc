package Fit;

import java.time.Instant;
import java.time.Duration;
import java.util.List;

/**
 * The activity data extracted from a FIT file.
 */
public record FitActivity(
        String deviceName,
        Instant startTime,
        Instant endTime,
        List<FitRecord> records) {

    private static final double METERS_PER_SECOND_TO_KILOMETERS_PER_HOUR = 3.6;

    public FitActivity {
        records = List.copyOf(records);
    }

    public Duration duration() {
        if (startTime == null || endTime == null) {
            return Duration.ZERO;
        }
        return Duration.between(startTime, endTime);
    }

    public Double averagePower() {
        return average(records.stream().map(FitRecord::power).toList());
    }

    public Double averageSpeed() {
        Double averageSpeed = average(records.stream().map(FitRecord::speed).toList());
        return averageSpeed == null ? null : averageSpeed * METERS_PER_SECOND_TO_KILOMETERS_PER_HOUR;
    }

    public Double averageCadence() {
        return average(records.stream().map(FitRecord::cadence).toList());
    }

    public Double averageHeartRate() {
        return average(records.stream().map(FitRecord::heartRate).toList());
    }

    private static Double average(List<? extends Number> values) {
        return values.stream()
                .filter(value -> value != null)
                .mapToDouble(Number::doubleValue)
                .average()
                .stream()
                .boxed()
                .findFirst()
                .orElse(null);
    }
}
