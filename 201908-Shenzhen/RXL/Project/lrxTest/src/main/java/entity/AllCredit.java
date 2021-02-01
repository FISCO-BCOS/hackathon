package entity;

public class AllCredit {

    public AllCredit(long tempSum, long tempOblgCredits, long tempOptCredits) {
        this.tempSum = tempSum;
        this.tempOblgCredits = tempOblgCredits;
        this.tempOptCredits = tempOptCredits;
    }

    @Override
    public String toString() {
        return "AllCredit{" +
                "tempSum=" + tempSum +
                ", tempOblgCredits=" + tempOblgCredits +
                ", tempOptCredits=" + tempOptCredits +
                '}';
    }

    public long getTempSum() {
        return tempSum;
    }

    public void setTempSum(long tempSum) {
        this.tempSum = tempSum;
    }

    public long getTempOblgCredits() {
        return tempOblgCredits;
    }

    public void setTempOblgCredits(long tempOblgCredits) {
        this.tempOblgCredits = tempOblgCredits;
    }

    public long getTempOptCredits() {
        return tempOptCredits;
    }

    public void setTempOptCredits(long tempOptCredits) {
        this.tempOptCredits = tempOptCredits;
    }

    private long tempOblgCredits;
    private long tempOptCredits;
    private long tempSum;
}
