package nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NLP {
    static String[] conjunctions = {"for","and","nor","but","or","yet","so"};
    static String[] preposition = {"with","at","from","into","during","including","until","against","among","throughout","despite","towards","upon","concerning","of","to","in","for","on","by","about","like","through","over","before","between","after","since","without","under","within",
    "along","following","across","behind","beyond","plus","except","but","up","out","around","down","off","above","near"};
    
    public static void main (String[] args){
        //WordTokenizer tokenizer = new WordTokenizer();
        
        Scanner scn = new Scanner(System.in);
        String input = "";
        //ViterbiAlgorithm va = new ViterbiAlgorithm();
        TagMapper mp = new TagMapper();
        //Con2000Loader CN = new Con2000Loader();
        /*if((new File("Model.slz")).exists() == false){
            try {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream("Model.slz"));
                obj.writeObject(CN);
                obj.close();
            } catch (IOException ex) {
                Logger.getLogger(NLP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }*/
       try{
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream("Model.slz"));
            try {
                Con2000Loader temp = (Con2000Loader)obj.readObject();
                String word = "the";
                for(String wordss:temp.getListofWords()){
                    if(temp.getListofWordsData().get(wordss).pos_tag_list.contains("PRP") || temp.getListofWordsData().get(wordss).pos_tag_list.contains("PRP"))
                        System.out.println(wordss);
                }
                System.out.println(temp.getListofWordsData().get(word).pos_tag_list + temp.getListofWordsData().get(word).pos_tag_frequency.toString());
                //System.out.println(temp.getListofPOSTags());
                //System.out.println(temp.getPOSTransFreq().get("DTNNP"));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NLP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(IOException e){
            System.out.println(e);
        }
        //Con2000Loader cl = new Con2000Loader();
        //System.out.println(cl.getListofPOSTags().toString());
        //Testing tst = new Testing();
    }
}
