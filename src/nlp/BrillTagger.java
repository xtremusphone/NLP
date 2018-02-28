package nlp;

import java.util.ArrayList;
import java.util.HashMap;

public class BrillTagger {
    
    public BrillTagger(){
        
    }
    
    public HashMap<String,String> correctPOSTaggingConll(HashMap<String,String> tagged,ArrayList<String> sentence){
        for(int i = 0 ; i < sentence.size() ; i++){
            String current = sentence.get(i);
            String previous = "<START>";
            if(i > 0){
                previous = sentence.get(i - 1);
            }
            //rule 1 : DT, {VBD | VBP} --> DT, NN
            if(i > 0 && tagged.get(previous).equals("DT")){
                if(tagged.get(current).equals("VBD") || tagged.get(current).equals("VBP") || tagged.get(current).equals("VB")){
                    tagged.put(current, "NN");
                }
            }
            
            //rule 2 : convert a noun to a number (CD) if "." appears in the word
            if(current.contains(".") && current.length() > 1){
                tagged.put(current, "CD");
            }
            
            //rule 3 : convert a noun to a past participle if words.get(i) ends with "ed"
            System.out.println(current + " : "+ tagged.get(current));
            if(tagged.get(current).startsWith("N") && current.endsWith("ed")){
                tagged.put(current, "VBN");
            }
            
            //rule 4 : convert any type to adverb if it ends in "ly"
            if(current.endsWith("ly")){
                tagged.put(current, "RB");
            }
            
            //rule 5 : convert a common noun (NN or NNS) to an adjective if it ends with "al"
            if(tagged.get(current).startsWith("NN") && current.endsWith("al")){
                tagged.put(current, "JJ");
            }
            
            //rule 6 : convert a noun to a verb if the preceding work is "would"
            if(i > 0 && tagged.get(previous).startsWith("NN") && previous.equalsIgnoreCase("would")){
                tagged.put(current, "VB");
            }
            
            //rule 7 : if a word has been categorized as a common anoun nd it ends with "s", then set its type to plural common noun (NNS)
            if(tagged.get(current).equals("NN") && current.endsWith("s")){
                tagged.put(current, "NNS");
            }
            
            //rule 8 : convert a common noun to a present participle verb (i.e., a gerund)
            if(tagged.get(current).endsWith("NN") && current.endsWith("ing")){
                tagged.put(current, "VBG");
            }
        }
        return tagged;
    }
}
