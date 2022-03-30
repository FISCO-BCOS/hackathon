package entity;

public class Activity {
    private int actID;
    private String actName;
    private String sponsor;
    private String status;
    private String extInfo;
    private long time;

    public Activity(int actID, String actName, String sponsor, String status, String extInfo, long time) {
        this.actID = actID;
        this.actName = actName;
        this.sponsor = sponsor;
        this.status = status;
        this.extInfo = extInfo;
        this.time = time;
    }

    public int getActID() {
        return actID;
    }

    public void setActID(int actID) {
        this.actID = actID;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return "Activity{" +
                "actID=" + actID +
                ", actName='" + actName + '\'' +
                ", sponsor='" + sponsor + '\'' +
                ", status='" + status + '\'' +
                ", extInfo='" + extInfo + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
