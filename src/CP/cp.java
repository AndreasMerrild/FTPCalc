package CP;

public class cp {

    public cp() {
    }

    /**
     * Calculates critical power from the best three-minute and twenty-minute
     * powers using the two-parameter work-capacity model.
     *
     * @param twenty best average power for twenty minutes, in watts
     * @param three best average power for three minutes, in watts
     * @param wprime retained for API compatibility; it is not needed when two
     *               maximal efforts are provided
     * @return critical power in watts
     */
    public double calcCP(double twenty, double three, int wprime) {
        if (!Double.isFinite(twenty) || !Double.isFinite(three)
                || twenty <= 0 || three <= 0 || three <= twenty) {
            throw new IllegalArgumentException(
                    "Twenty-minute power and three-minute power must be finite, positive values, "
                            + "and three-minute power must exceed twenty-minute power.");
        }

        // P = CP + W'/t, so two efforts give:
        // CP = (P20 * 1200 - P3 * 180) / (1200 - 180).
        return (twenty * 1200.0 - three * 180.0) / (1200.0 - 180.0);
    }
}
