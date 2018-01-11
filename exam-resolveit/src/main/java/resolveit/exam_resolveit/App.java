package resolveit.exam_resolveit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.StemAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;



/**
 * Hello world!
 *
 */
public class App 
{
	
	public ArrayList<TokenWord> tokenList = new ArrayList();
	
    public static void main( String[] args )
    {
       
        
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = "Take this paragraph of text and return an alphabetized list of ALL unique words.  A unique word is any form of a word often communicated with essentially the same meaning. For example, fish and fishes could be defined as a unique word by using their stem fish. For each unique word found in this entire paragraph, determine the how many times the word appears in total. Also, provide an analysis of what unique sentence index position or positions the word is found. The following words should not be included in your analysis or result set: \"a\", \"the\", \"and\", \"of\", \"in\", \"be\", \"also\" and \"as\".  Your final result MUST be displayed in a readable console output in the same format as the JSON sample object shown below.";//"book books";//"For example, fish and fishes could be defined as a unique word by using their stem fish. you buy a book and then you book a room in an hotel.";
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        
     
        
     // these are all the sentences in this document
     // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
     List<CoreMap> sentences = document.get(SentencesAnnotation.class);

     List<TokenWord> listword = new ArrayList<TokenWord>();
     List <Integer> sentList = new ArrayList<Integer>();
     int numSent= 0;
     boolean check = true;
     
     
     
     for(CoreMap sentence: sentences) {
       // traversing the words in the current sentence
       // a CoreLabel is a CoreMap with additional token-specific methods
       for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
         // this is the text of the token
         String word = token.get(TextAnnotation.class);
         // this is the POS tag of the token
         String pos = token.get(PartOfSpeechAnnotation.class);
         // this is the NER label of the token
         String ne = token.get(NamedEntityTagAnnotation.class);
       
         String lem = token.get(LemmaAnnotation.class);
         
         String stem = token.get(StemAnnotation.class);
         
         System.out.println("word " + word + " pos: " + pos + " ne :" + ne+ " lemma: " + lem + " stem: "  + stem);
 
         sentList.add(numSent);
         TokenWord tokenWord =  new TokenWord();
         tokenWord.setLem(lem);
         tokenWord.setPos(pos);
         tokenWord.setWord(word);
         tokenWord.setTotalOccurrences(1);
       
         
         if(check){
        	 	List <Integer> sentList1 = new ArrayList<Integer>();
        	 
        	 	sentList1.add(numSent);
        	 	tokenWord.setSentence(sentList1);
        	 	listword.add(tokenWord);
        	 	check =false;
        	 
         }else {
        	 	addWord(listword, tokenWord, numSent);
         }

       }
       numSent++;
      
     }
  
     formatListAndShow(listword);
        
    }
    
    
    private static void formatListAndShow(List<TokenWord> listword) {
    		
    		ArrayList<ObjectToJson> formatedList = new ArrayList();
    		
    	
    		Iterator <TokenWord> iterTW = listword.iterator();
		while(iterTW.hasNext()) {
			TokenWord tw = iterTW.next();
			
			ObjectToJson obj = new ObjectToJson();
			obj.setWord(tw.getWord());
			obj.setTotalOcurreces(tw.getTotalOccurrences());
			obj.setSentence(tw.getSentence());
			
			formatedList.add(obj);
		}
    	
    	
		Result result = new Result();
	     result.setResults(formatedList);
    	
	     
	     Gson gsonPrint = new GsonBuilder().setPrettyPrinting().create();
	     String show = gsonPrint.toJson(result);
	     
	     System.out.println(show);
		
	}


	public static void addWord(List<TokenWord> listword, TokenWord tokenWord, int numSent) {
    	
	    	if(tokenWord.getWord().equals("a") || tokenWord.getWord().equals("the") || tokenWord.getWord().equals("and") 
	    			|| tokenWord.getWord().equals("of") || tokenWord.getWord().equals("in") || tokenWord.getWord().equals("be") 
	    			|| tokenWord.getWord().equals("also") || tokenWord.getWord().equals("as") || tokenWord.getWord().equals(",") || tokenWord.getWord().equals(".") || tokenWord.getWord().equals(":") || tokenWord.getWord().equals("``") || tokenWord.getWord().equals("\u0027\u0027")  ) {
	    		
	    	}else {
	    		boolean add = true;
	    		Iterator <TokenWord> iterTW = listword.iterator();
	    		while(iterTW.hasNext()) {
	    			
	    			TokenWord tw = iterTW.next();
	    			boolean isIndex = tw.getPos().indexOf(tokenWord.getPos()) != -1;
	    			boolean isIndexx = tokenWord.getPos().indexOf(tw.getPos()) != -1;
	    			if(tw.getLem().equalsIgnoreCase(tokenWord.getLem()) && (isIndex || isIndexx)) {
	    				int sum = tw.getTotalOccurrences();
	    				sum++;
	    				tw.setTotalOccurrences(sum);
	    				tw.getSentence().add(numSent);
	    				add = false;
	    				break;
	    			}

	    			
	    		}
    		
    		
	    		if(add) {
	    			
	    		 	 List <Integer> sentList1 = new ArrayList<Integer>();
	            	 
	            	 sentList1.add(numSent);
	            	 tokenWord.setSentence(sentList1);
	    			
	    			listword.add(tokenWord);
	            	
	                System.out.println("sorting:");
	           	   Collections.sort(listword, tokenWord.WordNameComparator);
	    		}
    		
  
    		
    	}
    		
    		
    
    }   
    	
    
    
}
