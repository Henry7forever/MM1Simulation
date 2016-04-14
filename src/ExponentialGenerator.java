import java.util.ArrayList;

/**
 * Created by 1707lab on 2015/11/8.
 */
public class ExponentialGenerator {
    private static ArrayList<Double> expoGenerator(double lambda, int numOfsample) {
        //  1/lambda is mean value of the samples
        ArrayList<Double> seq = new ArrayList<Double>();

        //ramdomize generate n numbers follow exponential distribution
        for (int i = 0; i < numOfsample; i++) {
            double theSampleValue = -1 * ((double) Math.log(Math.random())) / lambda;
            seq.add(theSampleValue);
        }
        return seq;
    }


    //for generate packets length
    public static  ArrayList<Double> packetLengthSeq(double lambda, int numOfsample)
    {
        return ExponentialGenerator.expoGenerator(lambda , numOfsample);
    }


    //for generate inter arrival time list
    public static  ArrayList<Double> timeSeq(double lambda, int numOfsample)
    {
        ArrayList<Double> interTimeSeq = ExponentialGenerator.expoGenerator(lambda , numOfsample);
        ArrayList<Double> seq = new ArrayList<Double>();

        //transform to arrival timeline
        for (int i = 0; i < interTimeSeq.size(); i++) {
            if (i == 0)
                seq.add(interTimeSeq.get(0));
            else
                seq.add(seq.get(i - 1) + interTimeSeq.get(i));
        }
        return seq;
    }
}



