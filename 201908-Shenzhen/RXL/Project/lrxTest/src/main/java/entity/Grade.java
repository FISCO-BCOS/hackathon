package entity;

public class Grade {
    private long stuID;
    private String stuName;
    private int term;
    private int averageGrades;
    private String extInfo;
    private long time;

    public Grade(long stuID, String stuName, int term, int averageGrades, String extInfo, long time) {
        this.stuID = stuID;
        this.stuName = stuName;
        this.term = term;
        this.averageGrades = averageGrades;
        this.extInfo = extInfo;
        this.time = time;
    }

    public long getStuID() {
        return stuID;
    }

    public void setStuID(long stuID) {
        this.stuID = stuID;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getAverageGrades() {
        return averageGrades;
    }

    public void setAverageGrades(int averageGrades) {
        this.averageGrades = averageGrades;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "stuID=" + stuID +
                ", stuName='" + stuName + '\'' +
                ", term=" + term +
                ", averageGrades=" + averageGrades +
                ", extInfo='" + extInfo + '\'' +
                ", time=" + time +
                '}';
    }
}
