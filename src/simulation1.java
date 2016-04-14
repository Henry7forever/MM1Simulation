import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.opencsv.*;

/**
 * Created by 1707lab on 2016/4/11.
 */
public class Simulation1 {
    public static void main(String[] args) {

        int numOfSample;

        //simulation arguments
        numOfSample = 10000;
        long []memoryCapacity = {10000000 ,80000, 160000, 240000};
        double linkCapacity = 1E8;
        double meanPacketLength = 8E3 ;
        double []arrivalRate = {1000,3000,5000,7000,9000,11000};


        //Run
        for (long memCapacity : memoryCapacity) {
            for (double arrRate : arrivalRate) {
                  Simulation1.runSimulation(numOfSample, arrRate, linkCapacity, meanPacketLength, memCapacity);
            }
        }
    }


    public static void runSimulation(int numOfSample , double arrivalRate , double linkCapacity, double meanPacketLength ,long memoryCapacity)  {


        //generate arrival timelines and packets length
        ArrayList<Double> timeLine = ExponentialGenerator.timeSeq(arrivalRate, numOfSample);
        ArrayList<Double> packetSeq = ExponentialGenerator.packetLengthSeq( 1 / meanPacketLength, numOfSample);


        //create a queueing system instance
        QueueSystem aSystem = new QueueSystem(timeLine, packetSeq, linkCapacity, memoryCapacity);

        //packets log file
        ArrayList<Packet> thePacketLog = aSystem.getPacketLog();


        /*---------detail logger file for queueing system---------------*/
        /*
        for (Packet x : thePacketLog){
            System.out.println("ARR: "+x.getArrivalTime()+"\tDEP: "+x.getDepartureTime()+"\tDWELL: "+x.getDwellTime());
        }
        */


        //get the information we need
        double avgDwellTime = aSystem.averageDwellTime();
        double avgQueueingLength = aSystem.avgQlength();
        double dropRate = aSystem.blockingRate();
        long drops = aSystem.getNumOfDrops();



        System.out.println("memory capacity: " + memoryCapacity);
        System.out.println("Average Dwelling Time for arrivalRate " + (long)arrivalRate + ":\t" + avgDwellTime + " seconds\tAvgQLength: " + avgQueueingLength);
        System.out.println("blocking rate: "+dropRate+"\t" + "\tdrops: "+drops);

        ArrayList<String> dataInput = new ArrayList<>();

        String[] metaData = {"memory capacity","arrival rate","avg dwelling time","avg queue length","drops","drop rate"};

        //a line of buffering data to be written in .csv file
        dataInput.add(Long.toString(memoryCapacity));
        dataInput.add(Double.toString(arrivalRate));
        dataInput.add(Double.toString(avgDwellTime) );
        dataInput.add(Double.toString(avgQueueingLength));
        dataInput.add(Double.toString(drops));
        dataInput.add(Double.toString(dropRate));

        String[] data = dataInput.toArray(new String[dataInput.size()]);

        String path = "dataCollector.csv";
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(path , true) , ',');
        } catch (IOException e) {
            e.printStackTrace();
        }

        //write the buffer data into file
            assert writer != null;
            writer.writeNext(data);

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //for Test
        /*
        for (double x : timeLine) {
            System.out.println(x);
        }
        System.out.println("--------service time:--------------");

        for (double y : packetSeq) {
            System.out.println(y / 1E8);
        }
        */
    }

}
