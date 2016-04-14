import java.util.*;

/**
 * Created by 1707lab on 2016/4/11.
 */
public class QueueSystem {
    private ArrayList<Packet> packetLog;
    private long numOfPackets;
    private long memoryCapacity;
    private long numOfDrops;


    //initialize the queueing system ,generate the detail packet log file(include departure time)
    public QueueSystem(ArrayList<Double> timeLine, ArrayList<Double> packetSeq, double linkCapacity, long memoryCapacity) {
        this.packetLog = new ArrayList<Packet>();
        this.numOfPackets = packetSeq.size();
        this.memoryCapacity = memoryCapacity;
        this.numOfDrops =0;

        Queue<Packet> theQueue = new LinkedList<>();


        for (int i = 0; i < timeLine.size(); i++) {
            double arrTime = timeLine.get(i);
            double serviceTime = packetSeq.get(i) / linkCapacity;


            //normal case , need no wait
            if (i == 0) {
                packetLog.add(new Packet(arrTime, arrTime + serviceTime, serviceTime , packetSeq.get(i)));
            }
            else if (arrTime >= packetLog.get(i - 1).getDepartureTime()) {
                packetLog.add(new Packet(arrTime, arrTime + serviceTime, serviceTime ,packetSeq.get(i)));
            }
            //queueing case , need to wait
            else {
                long qLenNow = 0;
                long bufferSize = 0;

                //when next arrival occur ,count the queue length now
                while (theQueue.peek() != null) {
                    if (arrTime >= theQueue.peek().getDepartureTime()) {
                        //dequeue the already departure packets
                        theQueue.poll();
                    }

                    // count the remain packets in queue ,it is the qlenNow
                    else {
                        for (Packet y : theQueue)
                        {
                            bufferSize += y.getPacketLength();
                        }
                        break;
                    }
                }
                qLenNow = theQueue.size();

                //packet is accept to enter the queue
                if (bufferSize < memoryCapacity) {
                    double theDepartTime = packetLog.get(i - 1).getDepartureTime() + serviceTime;
                    packetLog.add(new Packet(arrTime, theDepartTime, theDepartTime - arrTime ,packetSeq.get(i)));
                    theQueue.offer(packetLog.get(i));
                }
                //dropped
                else
                {
                    packetLog.add(new Packet(arrTime, arrTime, 0 , packetSeq.get(i)));
                    numOfDrops++ ;
                }
            }

        }

    }

    public double averageDwellTime() {
        double totalDwellTime = 0;
        for (Packet x : packetLog) {
            totalDwellTime += x.getDwellTime();
        }
        return totalDwellTime / numOfPackets;
    }


    public double avgQlength() {
        //Queue<Packet> theQueue = new LinkedList<>();

        //qEvent for store events
        ArrayList<Event> qEvent = new ArrayList<>();

        //generate the events sequence ,and sort by time ,and classify the event types
        for (int i = 0; i < packetLog.size(); i++) {
            Packet thisPacket = packetLog.get(i);

            if (thisPacket.getDwellTime() == 0){
                qEvent.add(new Event("dropped", thisPacket.getDepartureTime()));
            }
            else {
                qEvent.add(new Event("arrival", thisPacket.getArrivalTime()));
                qEvent.add(new Event("departure", thisPacket.getDepartureTime()));
            }

            //sorting events to be a events on a timeline
            Collections.sort(qEvent, new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    if (o1.getOccurTime() - o2.getOccurTime() >= 0)
                        return (int) (Math.ceil(o1.getOccurTime() - o2.getOccurTime()));
                    else
                        return -1;
                }
            });
        }


        long qlength = 0;


        //go through all event and caculate the queue length when just after the event occur
        //recording the queue length and durTime of each event
        for (int i = 0; i < qEvent.size(); i++) {
            String thisEventType = qEvent.get(i).getEventType();
            double theEventTime = qEvent.get(i).getOccurTime();

            //first and last event never into queue
            if (i == 0) {
                double nextEventTime = qEvent.get(i + 1).getOccurTime();
                qEvent.get(i).setDurTime(nextEventTime);
            } else if (i == qEvent.size() - 1) {
                qEvent.get(i).setDurTime(0);
            } else {
                double nextEventTime = qEvent.get(i + 1).getOccurTime();
                String prevEventType = qEvent.get(i - 1).getEventType();

                //two conditions for going to queue
                if (thisEventType.equals("arrival") && (qlength > 0 || prevEventType.equals("arrival"))) {
                    qlength++;
                }
                else if (thisEventType.equals("departure") && qlength > 0) {
                    qlength--;
                }
                //recoring queue length and durTime in this event
                qEvent.get(i).setDurTime(nextEventTime - theEventTime);
                qEvent.get(i).setQueueLength(qlength);
            }

        }

        /* detail runtime testing!!
        for (int i = 0; i < qEvent.size(); i++) {
            System.out.println("(" + i + "): "+qEvent.get(i).getEventType()+"\t" + qEvent.get(i).getOccurTime() + "\tqLength: " + qEvent.get(i).getQueueLength() + "\tdurTime: " + qEvent.get(i).getDurTime());
        }
        */

        //caculate the productSum and /total time ,it's average queueing length
        double productSum = 0;
        for (Event x : qEvent) {
            productSum += x.getQueueLength() * x.getDurTime();
        }

        return productSum / qEvent.get(qEvent.size() - 1).getOccurTime();
    }


    public double blockingRate(){
     return (double)numOfDrops / (double)numOfPackets;
    }


    public ArrayList<Packet> getPacketLog() {
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

