package jp.thotta.android.proportiontest;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

/**
 * Created by thotta on 15/04/30.
 */
public class IndependenceTest {
    private long num1, num2, pos1, pos2;
    public double xSquare;
    public double pValue;

    public IndependenceTest(long sampleA, long positiveA, long sampleB, long positiveB) {
        num1 = sampleA;
        pos1 = positiveA;
        num2 = sampleB;
        pos2 = positiveB;
        execChiSquareTest();
    }

    public boolean isSignificantlyDifferent(double significanceLevel) {
        if(pValue < significanceLevel) {
            return true;
        } else {
            return false;
        }
    }

    private void execChiSquareTest() {
        long neg1 = num1 - pos1;
        long neg2 = num2 - pos2;
        long num = num1 + num2;
        long pos = pos1 + pos2;
        long neg = neg1 + neg2;
        double ePos1 = num1 * ((double)pos / num);
        double ePos2 = num2 * ((double)pos / num);
        double eNeg1 = num1 * ((double)neg / num);
        double eNeg2 = num2 * ((double)neg / num);
        double[] expected = {ePos1, ePos2, eNeg1, eNeg2};
        long[] observed = {pos1, pos2, neg1, neg2};
        ChiSquareTest t = new ChiSquareTest();
        xSquare = t.chiSquare(expected, observed);
        ChiSquaredDistribution distribution = new ChiSquaredDistribution(null, 1.0);
        pValue = 1.0 - distribution.cumulativeProbability(xSquare);
    }
}
