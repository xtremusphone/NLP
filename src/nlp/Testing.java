package nlp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Testing {
    
    private ViterbiAlgorithm va;
    
    public Testing(){
        va = new ViterbiAlgorithm();
        test();
    }
    
    public void test(){
        try{
            Scanner scn = new Scanner(new File("test.txt"));
            HashMap<String,String> actual = new HashMap<>();
            double cumulative_score = 0;
            String sequence = "";
            int accumulate = 1;
            while(scn.hasNext()){
                String word = scn.next();
                if(word.equals(".")){
                    scn.next();
                    scn.next();
                    System.out.println(sequence);
                    cumulative_score += evaluate(va.getPOSTagging(sequence),actual);
                    System.out.println("score :" + cumulative_score);
                    ++accumulate;
                    sequence = "";
                }
                else{
                    String tag = scn.next();
                    scn.next();
                    sequence += word + " ";
                    actual.put(word, tag);
                }
            }
            System.out.println("Total score of : "+ cumulative_score / accumulate);
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
    
    public double evaluate(HashMap<String,String> prediction,HashMap<String,String> actual){
        int correct = 0;
        int total = 0;
        for(String x:prediction.keySet()){
            System.out.println("[" + x +"] prediction :" + prediction.get(x) + " vs actual :" + actual.get(x));
            if(prediction.get(x).equals(actual.get(x)))
                ++correct;
            ++total;
        }
        return (double)correct / (double)total;
    }
}
