package entity;

public class Cert {
    private long certID;
    private long stuID;
    private String stuName;
    private String usName;
    private String major;
    private String studyTime;
    private String certStatus;
    private String extInfo;
    private long time;

    public Cert(long certID, long stuID, String stuName, String usName, String major, String studyTime, String certStatus, String extInfo, long time) {
        this.certID = certID;
        this.stuID = stuID;
        this.stuName = stuName;
        this.usName = usName;
        this.major = major;
        this.studyTime = studyTime;
        this.certStatus = certStatus;
        this.extInfo = extInfo;
        this.time = time;
    }

    public long getCertID() {
        return certID;
    }

    public void setCertID(long certID) {
        this.certID = certID;
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

    public String getUsName() {
        return usName;
    }

    public void setUsName(String usName) {
        this.usName = usName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }

    public String getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(String certStatus) {
        this.certStatus = certStatus;
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
        return "Cert{" +
                "certID=" + certID +
                ", stuID=" + stuID +
                ", stuName='" + stuName + '\'' +
                ", usName='" + usName + '\'' +
                ", major='" + major + '\'' +
                ", studyTime='" + studyTime + '\'' +
                ", certStatus='" + certStatus + '\'' +
                ", extInfo='" + extInfo + '\'' +
                ", time=" + time +
                '}';
    }
}
