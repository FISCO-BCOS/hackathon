package entity;

public class StuActivity {

    private int actID;
    private long stuID;
    private String actName;
    private String stuName;
    private String extInfo;
    private long time;
    private String actSignature;

    public StuActivity(int actID, long stuID, String actName, String stuName, String extInfo, long time, String actSignature) {
        this.actID = actID;
        this.stuID = stuID;
        this.actName = actName;
        this.stuName = stuName;
        this.extInfo = extInfo;
        this.time = time;
        this.actSignature = actSignature;
    }

    public int getActID() {
        return actID;
    }

    public void setActID(int actID) {
        this.actID = actID;
    }

    public long getStuID() {
        return stuID;
    }

    public void setStuID(long stuID) {
        this.stuID = stuID;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
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

    public String getActSignature() {
        return actSignature;
    }

    public void setActSignature(String actSignature) {
        this.actSignature = actSignature;
    }

    @Override
    public String toString() {
        return "StuActivity{" +
                "actID=" + actID +
                ", stuID=" + stuID +
                ", actName='" + actName + '\'' +
                ", stuName='" + stuName + '\'' +
                ", extInfo='" + extInfo + '\'' +
                ", time=" + time +
                ", actSignature='" + actSignature + '\'' +
                '}';
    }
}
