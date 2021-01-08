package model;

import java.util.Random;

public class Yatzy {
    // Face values of the 5 dice.
    // 1 <= values[i] <= 6.
    private int[] values = new int[5];

    // Number of times the 5 dice have been thrown.
    // 0 <= throwCount <= 3.
    private int throwCount = 0;

    // Random number generator.
    private Random random = new Random();

    /**
     * Returns the 5 face values of the dice.
     */
    public int[] getValues() {
        return values;
    }

    /**
     * Sets the 5 face values of the dice. Req: values contains 5 face values in
     * [1..6]. Note: This method is only meant to be used for test, and therefore
     * has package visibility.
     */
    void setValues(int[] values) {
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i];
        }
    }

    /**
     * Returns the number of times the 5 dice has been thrown.
     */
    public int getThrowCount() {
        return throwCount;
    }

    /**
     * Resets the throw count.
     */
    public void resetThrowCount() {
        throwCount = 0;
    }

    /**
     * Rolls the 5 dice. Only roll dice that are not hold. Req: holds contain 5
     * boolean values.
     */
    public void throwDice(boolean[] holds) {

        for (int i = 0; i < values.length; i++) {
            if (!holds[i]) {
                values[i] = random.nextInt(6) + 1;
            }
        }
        throwCount++;
    }

    public void resetValues() {
        for (int i = 0; i < values.length; i++) {
            values[i] = 0;
        }
    }

    // -------------------------------------------------------------------------

    /**
     * Returns all results possible with the current face values. The order of the
     * results is the same as on the score board. Note: This is an optional method.
     * Comment this method out, if you don't want use it.
     */
    public int[] getResults() {
        int[] results = new int[15];
        for (int i = 0; i <= 5; i++) {
            results[i] = sameValuePoints(i + 1);
        }
        results[6] = onePairPoints();
        results[7] = twoPairPoints();
        results[8] = threeSamePoints();
        results[9] = fourSamePoints();
        results[10] = fullHousePoints();
        results[11] = smallStraightPoints();
        results[12] = largeStraightPoints();
        results[13] = chancePoints();
        results[14] = yatzyPoints();

        return results;
    }

    // -------------------------------------------------------------------------

    // Returns an int[7] containing the frequency of face values.
    // Frequency at index v is the number of dice with the face value v, 1 <= v
    // <= 6.
    // Index 0 is not used.
    private int[] calcCounts() {

        int[] frequency = new int[7];

        for (int i = 0; i < values.length; i++) {
            frequency[values[i]]++;
        }
        return frequency;
    }

    /**
     * Returns same-value points for the given face value. Returns 0, if no dice has
     * the given face value. Requires: 1 <= value <= 6;
     */
    public int sameValuePoints(int value) {
        int result = 0;
        result = calcCounts()[value] * value;
        return result;
    }

    /**
     * Returns points for one pair (for the face value giving highest points).
     * Returns 0, if there aren't 2 dice with the same face value.
     */
    public int onePairPoints() {
        int sum = 0;
        int[] frequency = calcCounts();
        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] >= 2) {
                sum = i * 2;
            }
        }
        return sum;
    }

    /**
     * Returns points for two pairs (for the 2 face values giving highest points).
     * Returns 0, if there aren't 2 dice with one face value and 2 dice with a
     * different face value.
     */
    public int twoPairPoints() {
        int pair1 = 0;
        int pair2 = 0;
        int result = 0;
        int[] frequency = calcCounts();

        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] >= 2) {
                pair2 = pair1;
                pair1 = i;
            }
        }
        if (pair2 != 0) {
            result = (pair1 + pair2) * 2;
        }
        return result;
    }

    /**
     * Returns points for 3 of a kind. Returns 0, if there aren't 3 dice with the
     * same face value.
     */
    public int threeSamePoints() {
        int sum = 0;
        int[] frequency = calcCounts();

        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] >= 3) {
                sum = i * 3;
            }
        }
        return sum;
    }

    /**
     * Returns points for 4 of a kind. Returns 0, if there aren't 4 dice with the
     * same face value.
     */
    public int fourSamePoints() {
        int sum = 0;
        int[] frequency = calcCounts();

        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] >= 4) {
                sum = i * 4;
            }
        }
        return sum;
    }

    /**
     * Returns points for full house. Returns 0, if there aren't 3 dice with one
     * face value and 2 dice a different face value.
     */
    public int fullHousePoints() {
        int[] frequency = calcCounts();
        int result = 0;
        int ens = 0;
        int par = 0;

        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] == 3) {
                ens = i;
            }
            if (frequency[i] == 2) {
                par = i;
            }

            if (ens != 0 && par != 0) {
                result = ens * 3 + par * 2;
            }
        }
        return result;
    }

    /**
     * Returns points for small straight. Returns 0, if the dice are not showing
     * 1,2,3,4,5.
     */
    public int smallStraightPoints() {
        int[] frequency = calcCounts();
        int result = 0;

        if (frequency[1] == 1) {
            if (frequency[2] == 1) {
                if (frequency[3] == 1) {
                    if (frequency[4] == 1) {
                        if (frequency[5] == 1) {
                            result = 15;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns points for large straight. Returns 0, if the dice is not showing
     * 2,3,4,5,6.
     */
    public int largeStraightPoints() {
        int[] frequency = calcCounts();
        int result = 0;

        if (frequency[2] == 1) {
            if (frequency[3] == 1) {
                if (frequency[4] == 1) {
                    if (frequency[5] == 1) {
                        if (frequency[6] == 1) {
                            result = 20;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns points for chance.
     */
    public int chancePoints() {
        int[] frequency = calcCounts();
        int result = 0;

        for (int i = 0; i < frequency.length; i++) {
            result += frequency[i] * i;
        }
        return result;
    }

    /**
     * Returns points for yatzy. Returns 0, if there aren't 5 dice with the same
     * face value.
     */
    public int yatzyPoints() {
        int result = 0;
        int[] frequency = calcCounts();

        for (int i = 0; i < frequency.length; i++) {
            if (frequency[i] == 5) {
                result = 50;
            }
        }
        return result;
    }
}
