package nlp;

import java.util.List;
import java.util.Scanner;

public class NLP {
    static String[] conjunctions = {"for","and","nor","but","or","yet","so"};
    static String[] preposition = {"with","at","from","into","during","including","until","against","among","throughout","despite","towards","upon","concerning","of","to","in","for","on","by","about","like","through","over","before","between","after","since","without","under","within",
    "along","following","across","behind","beyond","plus","except","but","up","out","around","down","off","above","near"};
    
    public static void main (String[] args){
        WordTokenizer tokenizer = new WordTokenizer();
        //CorpusLoader crp = new CorpusLoader();
        
        
        /*Scanner scn = new Scanner(System.in);
        String input = "";
        while(!input.equals("x")){
            System.out.println("Enter new text:");
            input = scn.nextLine();
            List<String> tokens = tokenizer.tokenizer(input);
            for(String x:tokens){
                if(crp.getListofWords().indexOf(x.toLowerCase()) != -1){
                    System.out.print(crp.getListofWordsData().get(crp.getListofWords().indexOf(x.toLowerCase())).pos_tag_list.toString() + " ");
                }
                else{
                    System.out.print("UNK" + " ");
                }
            }
            System.out.println("");
        }
        */
        
        String asd = "wae";
        AutoCorrect ac = new AutoCorrect(asd);
        System.out.println(ac.spellChecker(asd));
        
    }
}
