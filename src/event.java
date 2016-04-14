/**
 * Created by Andy on 2016/4/13.
 */
public class event {
    private String eventType ;
    private double occurTime ;
    private double durTime;
    private  long queueLength;

    public event(String eventType, double occurTime) {
        this.eventType = eventType;
        this.occurTime = occurTime;
        this.durTime = 0;
        this.queueLength = 0;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public double getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(double occurTime) {
        this.occurTime = occurTime;
    }

    public double getDurTime() {
        return durTime;
    }

    public void setDurTime(double durTime) {
        this.durTime = durTime;
    }

    public long getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(long queueLength) {
        this.queueLength = queueLength;
    }
}

