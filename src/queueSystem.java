import java.util.*;

/**
 * Created by 1707lab on 2016/4/11.
 */
public class queueSystem {
    private ArrayList<packet> packetLog;
    private long numOfPackets;
    private long memoryCapacity;
    private long numOfDrops;

    public queueSystem(ArrayList<Double> timeLine, ArrayList<Double> packetSeq, double linkCapacity, long memoryCapacity) {
        this.packetLog = new ArrayList<packet>();
        this.numOfPackets = packetSeq.size();
        this.memoryCapacity = memoryCapacity;
        this.numOfDrops =0;

        Queue<packet> theQueue = new LinkedList<>();


        for (int i = 0; i < timeLine.size(); i++) {
            double arrTime = timeLine.get(i);
            double serviceTime = packetSeq.get(i) / linkCapacity;

            if (i == 0) {
                packetLog.add(new packet(arrTime, arrTime + serviceTime, serviceTime , packetSeq.get(i)));
            } else if (arrTime >= packetLog.get(i - 1).getDepartureTime()) {
                packetLog.add(new packet(arrTime, arrTime + serviceTime, serviceTime ,packetSeq.get(i)));
            } else {


                long qLenNow = 0;
                long bufferSize = 0;
                while (theQueue.peek() != null) {
                    if (arrTime >= theQueue.peek().getDepartureTime()) {
                        theQueue.poll();
                    }
                    else {
                        for (packet y : theQueue)
                        {
                            bufferSize += y.getPacketLength();
                        }
                        break;
                    }
                }
                qLenNow = theQueue.size();
                if (bufferSize < memoryCapacity) {
                    double theDepartTime = packetLog.get(i - 1).getDepartureTime() + serviceTime;
                    packetLog.add(new packet(arrTime, theDepartTime, theDepartTime - arrTime ,packetSeq.get(i)));
                    theQueue.offer(packetLog.get(i));
                }
                else
                {
                    packetLog.add(new packet(arrTime, arrTime, 0 , packetSeq.get(i)));
                    numOfDrops++ ;
                }
            }

        }

    }

    public double averageDwellTime() {
        double totalDwellTime = 0;
        for (packet x : packetLog) {
            totalDwellTime += x.getDwellTime();
        }
        return totalDwellTime / numOfPackets;
    }

    public double avgQlength() {
        //Queue<packet> theQueue = new LinkedList<>();

        ArrayList<event> qEvent = new ArrayList<>();

        for (int i = 0; i < packetLog.size(); i++) {
            packet thisPacket = packetLog.get(i);

            if (thisPacket.getDwellTime() == 0){
                qEvent.add(new event("dropped", thisPacket.getDepartureTime()));
            }
            else {
                qEvent.add(new event("arrival", thisPacket.getArrivalTime()));
                qEvent.add(new event("departure", thisPacket.getDepartureTime()));
            }

            Collections.sort(qEvent, new Comparator<event>() {
                @Override
                public int compare(event o1, event o2) {
                    if (o1.getOccurTime() - o2.getOccurTime() >= 0)
                        return (int) (Math.ceil(o1.getOccurTime() - o2.getOccurTime()));
                    else
                        return -1;
                }
            });
        }


        long qlength = 0;

        for (int i = 0; i < qEvent.size(); i++) {
            String thisEventType = qEvent.get(i).getEventType();
            double theEventTime = qEvent.get(i).getOccurTime();

            if (i == 0) {
                double nextEventTime = qEvent.get(i + 1).getOccurTime();
                qEvent.get(i).setDurTime(nextEventTime);
            } else if (i == qEvent.size() - 1) {
                qEvent.get(i).setDurTime(0);
            } else {
                double nextEventTime = qEvent.get(i + 1).getOccurTime();
                String prevEventType = qEvent.get(i - 1).getEventType();
                if (thisEventType.equals("arrival") && (qlength > 0 || prevEventType.equals("arrival"))) {
                    qlength++;
                }
                else if (thisEventType.equals("departure") && qlength > 0) {
                    qlength--;
                }
                qEvent.get(i).setDurTime(nextEventTime - theEventTime);
                qEvent.get(i).setQueueLength(qlength);
            }

        }

        /*
        for (int i = 0; i < qEvent.size(); i++) {
            System.out.println("(" + i + "): "+qEvent.get(i).getEventType()+"\t" + qEvent.get(i).getOccurTime() + "\tqLength: " + qEvent.get(i).getQueueLength() + "\tdurTime: " + qEvent.get(i).getDurTime());
        }
        */

        double productSum = 0;
        for (event x : qEvent) {
            productSum += x.getQueueLength() * x.getDurTime();
        }

        return productSum / qEvent.get(qEvent.size() - 1).getOccurTime();
    }

    public double blockingRate(){

     return (double)numOfDrops / (double)numOfPackets;
    }


    public ArrayList<packet> getPacketLog() {
        return packetLog;
    }


    public long getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(long memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    public long getNumOfDrops() {
        return numOfDrops;
    }

    public void setNumOfDrops(long numOfDrops) {
        this.numOfDrops = numOfDrops;
    }
}

