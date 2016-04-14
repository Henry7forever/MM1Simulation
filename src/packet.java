import java.util.ArrayList;

/**
 * Created by 1707lab on 2016/4/11.
 */
public class packet {
    private double arrivalTime;
    private double departureTime;
    private double dwellTime;
    private double packetLength;


    public packet(double arrivalTime, double departureTime, double dwellTime ,double packetLength) {
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.dwellTime = dwellTime;
        this.packetLength = packetLength;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public double getDwellTime() {
        return dwellTime;
    }

    public double getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(double packetLength) {
        this.packetLength = packetLength;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public void setDwellTime(double dwellTime) {
        this.dwellTime = dwellTime;
    }
}
