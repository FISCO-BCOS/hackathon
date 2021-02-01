package entity;

public class Evaluation {
    @Override
    public String toString() {
        return "Evaluation{" +
                "M1=" + M1 +
                ", M2=" + M2 +
                ", M3=" + M3 +
                ", score=" + score +
                '}';
    }

    private long M1;

    public long getM1() {
        return M1;
    }

    public void setM1(long m1) {
        M1 = m1;
    }

    public long getM2() {
        return M2;
    }

    public void setM2(long m2) {
        M2 = m2;
    }

    public long getM3() {
        return M3;
    }

    public void setM3(long m3) {
        M3 = m3;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    private long M2;

    public Evaluation(long m1, long m2, long m3, long score) {
        M1 = m1;
        M2 = m2;
        M3 = m3;
        this.score = score;
    }

    private long M3;
    private long score;

}
