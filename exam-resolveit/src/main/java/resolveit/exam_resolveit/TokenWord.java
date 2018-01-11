package resolveit.exam_resolveit;

import java.util.Comparator;
import java.util.List;

public class TokenWord implements Comparable<TokenWord>{
	
	private String word;
	private String pos;
	private String lem;
	private List<Integer> sentence;
	private int totalOccurrences;
	
	
	public int getTotalOccurrences() {
		return totalOccurrences;
	}
	public void setTotalOccurrences(int totalOccurrences) {
		this.totalOccurrences = totalOccurrences;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getLem() {
		return lem;
	}
	public void setLem(String lem) {
		this.lem = lem;
	}
	
	public List<Integer> getSentence() {
		return sentence;
	}
	public void setSentence(List<Integer> sentence) {
		this.sentence = sentence;
	}
	public int compareTo(TokenWord o) {
		return this.word.compareTo(o.getWord());
		
	}
	
	
	
	public static Comparator<TokenWord> WordNameComparator = new Comparator<TokenWord>() {

		public int compare(TokenWord t1, TokenWord t2) {
		   String tokW1 = t1.getWord().toUpperCase();
		   String tokw2 = t2.getWord().toUpperCase();

		   //ascending order
		   return tokW1.compareTo(tokw2);

		  
	    }};

}
