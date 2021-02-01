package entity;

public class Student {
    private long stuId;
    private String stuName;
    private String usName;
    private int usLevel;
    private String major;
    private String extInfo;
    private long time;
    private int grade;

    public Student(long stuId, String stuName, String usName, int usLevel, String major, String extInfo, long time, int grade) {
        this.stuId = stuId;
        this.stuName = stuName;
        this.usName = usName;
        this.usLevel = usLevel;
        this.major = major;
        this.extInfo = extInfo;
        this.time = time;
        this.grade = grade;
    }

    public long getStuId() {
        return stuId;
    }

    public void setStuId(long stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getUsName() {
        return usName;
    }

    public void setUsName(String usName) {
        this.usName = usName;
    }

    public int getUsLevel() {
        return usLevel;
    }

    public void setUsLevel(int usLevel) {
        this.usLevel = usLevel;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuId=" + stuId +
                ", stuName='" + stuName + '\'' +
                ", usName='" + usName + '\'' +
                ", usLevel=" + usLevel +
                ", major='" + major + '\'' +
                ", extInfo='" + extInfo + '\'' +
                ", time=" + time +
                ", grade=" + grade +
                '}';
    }
}
