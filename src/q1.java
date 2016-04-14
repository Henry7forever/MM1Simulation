import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by 1707lab on 2015/11/8.
 */
public class q1 {
    private static ArrayList<Double> expoGenerator(double lambda, int numOfsample) {
        //  1/lambda is mean value of the samples
        ArrayList<Double> seq = new ArrayList<Double>();

        for (int i = 0; i < numOfsample; i++) {
            double theSampleValue = -1 * ((double) Math.log(Math.random())) / lambda;
            seq.add(theSampleValue);
        }
        return seq;
    }

    public static  ArrayList<Double> packetLengthSeq(double lambda, int numOfsample)
    {
        return q1.expoGenerator(lambda , numOfsample);
    }

    public static  ArrayList<Double> timeSeq(double lambda, int numOfsample)
    {
        ArrayList<Double> interTimeSeq = q1.expoGenerator(lambda , numOfsample);
        ArrayList<Double> seq = new ArrayList<Double>();
        for (int i = 0; i < interTimeSeq.size(); i++) {
            if (i == 0)
                seq.add(interTimeSeq.get(0));
            else
                seq.add(seq.get(i - 1) + interTimeSeq.get(i));
        }
        return seq;
    }




   // public static void main(String[] args) {

  /*
        int numOfSample;
        numOfSample = 80;
        ArrayList<Double> interTimeSeq = q1.expoGenerator(8000, numOfSample);
        ArrayList<Double> packetLengthSeq = q1.expoGenerator((double) 1 / 8000, numOfSample);
        ArrayList<Double> timeSeq = new ArrayList<Double>();

        for (int i = 0; i < interTimeSeq.size(); i++) {
            if (i == 0)
                timeSeq.add(interTimeSeq.get(0));
            else
                timeSeq.add(timeSeq.get(i - 1) + interTimeSeq.get(i));
        }

*/




    //}
}



