package entity;

public class Grades {
    private long stuID;
    private String stuName;
    private int grade;
    private int averageGrades;
    private String extInfo;
    private long time;

    public Grades(long stuID, String stuName, int grade, int averageGrades, String extInfo, long time) {
        this.stuID = stuID;
        this.stuName = stuName;
        this.grade = grade;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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
        return "Grades{" +
                "stuID=" + stuID +
                ", stuName='" + stuName + '\'' +
                ", grade=" + grade +
                ", averageGrades=" + averageGrades +
                ", extInfo='" + extInfo + '\'' +
                ", time=" + time +
                '}';
    }
}
