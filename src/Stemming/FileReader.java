package Stemming;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import com.aliasi.tokenizer.LineTokenizerFactory;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

public class FileReader {
	
	// all words 
	ArrayList<String> wordList = new ArrayList<String>(); 
	ArrayList<Integer> numOfTimeWordAppearList = new ArrayList<Integer>();
	
	// run the same index as wordList, contain stem
	ArrayList<String> originalToStemList = new ArrayList<String>(); 
	
	// all stem words
	ArrayList<String> stemWordList = new ArrayList<String>(); 
	ArrayList<Integer> numOfTimeStemWordAppearList = new ArrayList<Integer>();
	
	// all sentence
	ArrayList<String> sentenceList = new ArrayList<String>();
	
	// THIS ARRAYLIST CONTAIN INDEX of top word or stem word
	ArrayList<Integer> top20WordIndex = new ArrayList<Integer>();
	ArrayList<Integer> top20StemWordIndex = new ArrayList<Integer>();
	
	// for stemming purpose
	TokenizerFactory tokenizerFactory =LineTokenizerFactory.INSTANCE;	
	PorterStemmerTokenizerFactory porterStemmerTokenizerFactory= new PorterStemmerTokenizerFactory(tokenizerFactory);	

	public void readFile(){		
		String line;
		int numOfQuestion=0;
		int numOfAnswer=0;
		int numOfQuestionWith0Ans=0;
		int numOfQuestionWith1Ans=0;
		int numOfQuestionWith2Ans=0;
		int numOfQuestionWith3OrMoreAns=0;
			
		// parent id is the id of the question post 
		ArrayList<String> parentIdList = new ArrayList<String>();  
		ArrayList<Integer> numOfAnsList = new ArrayList<Integer>(); 
		
		
		int numOfWordInPost;
		int numOfPostWithWordRange1to100=0;
		int numOfPostWithWordRange101to200=0;
		int numOfPostWithWordRange201to300=0;
		int numOfPostWithWordRange301to400=0;
		int numOfPostWithWordRange401to500=0;
		int numOfPostWithWordRangeMoreThan500=0;
		int totalNumberOfPost;
		
		String stemWord;
		
		int numOfPostForAnnotation=0;
		
		
		try {
            System.out.println("Processing...");
		    InputStream inputStream = new FileInputStream("resources/Posts-10000.xml");
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(inputStreamReader);
		    Pattern pattern ;
		    Matcher matcher;
		    File file = new File("allPostWithAPI.txt");
		    FileWriter fw = new FileWriter(file.getAbsoluteFile());
		    BufferedWriter bw = new BufferedWriter(fw);
		    
		    StringUtils stringUtils= new StringUtils();
				while ((line = br.readLine()) != null) {	
					
					// usually 1 line in Posts-10000.xml is 1 post
					// find content of post
					pattern = Pattern.compile("Body=\"(.*?)\" OwnerUserId");
				    matcher = pattern.matcher(line);
				    
				    
				    if (matcher.find()){// found the content of post in the line
				    	// get content
				    	String postContent=matcher.group(1);
				    	if(containAPIKeyWord(postContent) && numOfPostForAnnotation<100){
			    		
				    		String formattedPostContent=Jsoup.parse(postContent).text() ;
				    		formattedPostContent=stringUtils.unescapeHtml3(formattedPostContent);
				    		formattedPostContent = formattedPostContent.replaceAll("<[^>]*>", "");
				    		
				    		//System.out.println("postContent is : "+postContent);
				    		//System.out.println("formattedPostContent is : "+formattedPostContent);
				    		
					    	bw.write(formattedPostContent);
				
							bw.newLine();
							bw.newLine();
							bw.newLine();
							bw.newLine();
							numOfPostForAnnotation++;
							
				    	}			   	
				    	// pattern for sentence
				    	pattern = Pattern.compile("[.!?][\\s](([a-zA-Z]+[\\s])+[a-zA-Z]+[.!?])");				    	
					    matcher = pattern.matcher(postContent);		
					    
					    while (matcher.find()) {// found a sentence						    	
							sentenceList.add(matcher.group(1));
							//System.out.println("The sentence is :  " + matcher.group(1));									
					    }
				    
				    	numOfWordInPost=0;
				    	// pattern for word
						pattern = Pattern.compile("([a-zA-Z]+)([\\s]|[:,!?').\"-])");					
					    matcher = pattern.matcher(postContent);						
					    while (matcher.find()) {// found a word						
							numOfWordInPost++;
							//System.out.println("Word found is :  " + matcher.group(1));
							// create array of word
							if (! isWordAStopWord(matcher.group(1))){// not a stop word
								
								stemWord=porterStemmerTokenizerFactory.stem(matcher.group(1));
								
								// update number of time the stem word appear
								if (stemWordList.contains(stemWord))
						    		numOfTimeStemWordAppearList.set(stemWordList.indexOf(stemWord), 
						    				numOfTimeStemWordAppearList.get(stemWordList.indexOf(stemWord))+1);
						    	else {
						    		stemWordList.add(stemWord);
						    		numOfTimeStemWordAppearList.add(1);
								}
							
								// update number of time the word appear
								if (wordList.contains(matcher.group(1)))
						    		numOfTimeWordAppearList.set(wordList.indexOf(matcher.group(1)), 
						    				numOfTimeWordAppearList.get(wordList.indexOf(matcher.group(1)))+1);
						    	else {
						    		wordList.add(matcher.group(1));
						    		numOfTimeWordAppearList.add(1);
						    		originalToStemList.add(stemWord);
								}
								
							}						
					    }
					    
					    // count number of word in a post
					    if(numOfWordInPost>=1 && numOfWordInPost<= 100)
							numOfPostWithWordRange1to100++;
						else if(numOfWordInPost>=101 && numOfWordInPost<= 200)
							numOfPostWithWordRange101to200++;
						else if(numOfWordInPost>=201 && numOfWordInPost<= 300)
							numOfPostWithWordRange201to300++;
						else if(numOfWordInPost>=301 && numOfWordInPost<= 400)
							numOfPostWithWordRange301to400++;
						else if(numOfWordInPost>=401 && numOfWordInPost<= 500)
							numOfPostWithWordRange401to500++;
						else 
							numOfPostWithWordRangeMoreThan500++;
					
					}// end of found content of post in a line of Posts-10000.xml
				
				    // update question got how many answer
					pattern = Pattern.compile("ParentId=\"(.*?)\"");
				    matcher = pattern.matcher(line);
				    if  (matcher.find()){
				    	if (parentIdList.contains(matcher.group(1)))
				    		numOfAnsList.set(parentIdList.indexOf(matcher.group(1)), 
				    				numOfAnsList.get(parentIdList.indexOf(matcher.group(1)))+1);
				    	else {
				    		parentIdList.add(matcher.group(1));
				    		numOfAnsList.add(1);
						}	    		
				    }					
				    // increase counter based on the type of post
					if (line.contains("PostTypeId=\"1\""))
						numOfQuestion++;	
					
					else if (line.contains("PostTypeId=\"2\""))
						numOfAnswer++;
					
			    }// end of while read each line
				
				// get statistics of question with how many answer 
				for (int i=0; i< parentIdList.size(); i++){
					if (numOfAnsList.get(i)==1)
						numOfQuestionWith1Ans++;
					else if (numOfAnsList.get(i)==2)
						numOfQuestionWith2Ans++;
					else
						numOfQuestionWith3OrMoreAns++;					
				}
				
				// question with no ans will never have answer with parent id of itself 
				numOfQuestionWith0Ans=numOfQuestion-numOfQuestionWith1Ans-numOfQuestionWith2Ans
						-numOfQuestionWith3OrMoreAns;
				System.out.println("Number of question is "+ numOfQuestion);
				System.out.println("Number of answer is "+ numOfAnswer);
				System.out.println("Number of question with no answer is "+ numOfQuestionWith0Ans);
				System.out.println("Number of question with 1 answer is "+ numOfQuestionWith1Ans);
				System.out.println("Number of question with 2 answer is "+ numOfQuestionWith2Ans);
				System.out.println("Number of question with 3 or more answer is "+ numOfQuestionWith3OrMoreAns);
				
				double tempDouble;
				tempDouble=(double) numOfQuestionWith0Ans/numOfQuestion*100;
				System.out.println("% of question with no answer is "+tempDouble );
				tempDouble=(double) numOfQuestionWith1Ans/numOfQuestion*100;
				System.out.println("% of question with 1 answer is "+ tempDouble);
				tempDouble=(double) numOfQuestionWith2Ans/numOfQuestion*100;
				System.out.println("% of question with 2 answer is "+ tempDouble);
				tempDouble=(double) numOfQuestionWith3OrMoreAns/numOfQuestion*100;
				System.out.println("% of question with 3 or more answer is "+ tempDouble);
				
				System.out.println("Number of post with number of words range 1 to 100 is "+ numOfPostWithWordRange1to100);
				System.out.println("Number of post with number of words range 101 to 200 is "+ numOfPostWithWordRange101to200);
				System.out.println("Number of post with number of words range 201 to 300 is "+ numOfPostWithWordRange201to300);
				System.out.println("Number of post with number of words range 301 to 400 is "+ numOfPostWithWordRange301to400);
				System.out.println("Number of post with number of words range 401 to 500 is "+ numOfPostWithWordRange401to500);
				System.out.println("Number of post with number of words range > 500 is "+ numOfPostWithWordRangeMoreThan500);
				
				findOutTop20words();
				findOutTop20StemWords();
				applyPOSTagging();
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	// This function checks whether the postContent contains the api in API.text
	// Although API.text does not contain all possible API, 
	// it ensures that the post selected for annotation contain at least 1 api
	public boolean containAPIKeyWord(String postContent) {
		
		String line;
		try {
		    InputStream inputStream = new FileInputStream("resources/API.txt");
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(inputStreamReader);
		    while ((line = br.readLine()) != null ) {
		    	
		    	if ( postContent.toLowerCase().contains("."+line.toLowerCase())){	
		    		//System.out.println("so what the line is "+line);
		    		return true;
		    	}
		    }
		    inputStream.close();
            inputStreamReader.close();
		    return false;
		}
		catch(Exception e){			
			e.printStackTrace();
			return false;
		}
	}

	public void applyPOSTagging(){
		int randomNumber;
		Random random = new Random();
		ArrayList<Integer> random10Integer = new ArrayList<Integer>();
		ArrayList<String> random10Sentence = new ArrayList<String>();
		
		for (int i=0; i<10; i++){// get 10 random number 
			randomNumber = random.nextInt(sentenceList.size()) ;
			while (random10Integer.contains(randomNumber))
				randomNumber = random.nextInt(sentenceList.size()) ;
			random10Integer.add(randomNumber);
		}
		for (int j=0; j<10; j++){// get 10 random sentence 
			random10Sentence.add(sentenceList.get(random10Integer.get(j)));
			System.out.println(sentenceList.get(random10Integer.get(j)));
		}
		
		// Now we have 10 random sentence in random10Sentence
		
	}
	
	public void findOutTop20words(){// print out all the top 20 words
		
		for (int j=0; j<20; j++){
			
			int topIndex=0;
			for (int i=1; i<numOfTimeWordAppearList.size(); i++){
				if (numOfTimeWordAppearList.get(i)>numOfTimeWordAppearList.get(topIndex))
					topIndex=i;
			}
			top20WordIndex.add(topIndex);
			numOfTimeWordAppearList.set(topIndex, 0);
			
		}
		
		System.out.println("The top 20 words without stemming start with the top word are ");
		for (int k=0; k<top20WordIndex.size(); k++){
			String topWord=wordList.get(top20WordIndex.get(k));
			System.out.println(""+ (k+1) +" " + topWord);
			
		}
	}
	
public void findOutTop20StemWords(){// print out all the top 20 stem word
		
		for (int j=0; j<20; j++){
			
			int topIndex=0;
			for (int i=1; i<numOfTimeStemWordAppearList.size(); i++){
				if (numOfTimeStemWordAppearList.get(i)>numOfTimeStemWordAppearList.get(topIndex))
					topIndex=i;
			}
			top20StemWordIndex.add(topIndex);
			numOfTimeStemWordAppearList.set(topIndex, 0);
			
		}
		
		System.out.println("The top 20 stem words start with the top word are ");
		for (int k=0; k<top20StemWordIndex.size(); k++){
			String topWord=stemWordList.get(top20StemWordIndex.get(k));
			System.out.println(""+ (k+1) +" " + topWord + translateStemToOriginal(topWord));// top stem word 
			
			
		}
	}

// get all the original word of the stem word
public String translateStemToOriginal(String topStemWord){
	String originalWord;
	String message="";
	
	for (int i=0; i<originalToStemList.size(); i++){
		if (originalToStemList.get(i).equals(topStemWord)){
			originalWord=wordList.get(i);
			if (i!=originalToStemList.size()-1)
				message=message+originalWord+", ";
			else 
				message=message+originalWord +".";
			
		}		
	}
	message="- The original words for "+ topStemWord +" are " + message;
	return message;
	
}
	
	public boolean isWordAStopWord(String word){
		String line;
		try {
            InputStream inputStream = new FileInputStream("resources/stopword.txt");
		    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(inputStreamReader);
		    while ((line = br.readLine()) != null) {
		    	if ( line.equals(word.toLowerCase()) || word.length()==1){
		    		//System.out.println("Stop word detected");;
		    		return true;
		    	}
		    }
            inputStream.close();
            inputStreamReader.close();
		    return false;
		}
		catch(Exception e){			
			e.printStackTrace();
			return false;
		}
		
	}
	
	
public int countWords(String s){// count number of word in a string 

    int wordCount = 0;

    boolean word = false;
    int endOfLine = s.length() - 1;

    for (int i = 0; i < s.length(); i++) {
        // if the char is a letter, word = true.
        if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
            word = true;
            // if char isn't a letter and there have been letters before,
            // counter goes up.
        } else if (!Character.isLetter(s.charAt(i)) && word) {
            wordCount++;
            word = false;
            // last word of String; if it doesn't end with a non letter, it
            // wouldn't count without this.
        } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
            wordCount++;
        }
    }
    return wordCount;
}


}
