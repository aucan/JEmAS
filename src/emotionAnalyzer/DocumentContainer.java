package emotionAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.print.DocFlavor.READER;

import com.google.common.collect.HashMultiset;

/**
 * contains document path, settings for the processing and its results and
 * statistics
 * 
 * @author buechel
 * 
 */
public class DocumentContainer {

	final File document;
	final File normalizedDocument;
	final File documentTermVector;

	// final private String documentPath;
	// final private File documentFile;
	// Preprocessing usedPreprocessing;

	final private String reportCategory;
	final private String origin;
	final private String organization;
	final private String year;
	/**
	 * Number of "letter tokens" (Tokens which purely of letters and can
	 * therefore be regarded as "real words". This deviation may be important in
	 * this context to interprete the difference between token count and count
	 * of identified tokens during look-up because especially in annual reports,
	 * many tokens may be numbers.)
	 */

	private int tokenCount;
	private int normalizedTokenCount;
	/**
	 * Number of word vectors which contribute to the document vector. Unlike in
	 * prior versions, only the words which can be found in the lexicon
	 * contribute to the vector count (unidentified words will be evaluated as
	 * null vecotor and not neutral vector anymore.)
	 */
	private int recognizedTokenCount;
	// final Settings settings;

	EmotionVector documentEmotionVector;
	EmotionVector standardDeviationVector;

	public DocumentContainer(File givenDocument,
			File givenNormalizedDocumentFolder,
			File givenDocumentTermVectorFolder) {
		// initialize final fields for files of the document, the normalized
		// document and the document-term-vector
		this.document = givenDocument;
		this.normalizedDocument = new File(
				givenNormalizedDocumentFolder.getPath()
						+ givenDocument.getName());
		this.documentTermVector = new File(
				givenDocumentTermVectorFolder.getPath()
						+ givenDocument.getName());
		// Initialize the attributes of the document as coded in the filename.
		String[] nameParts = this.document.getName().split("\\.");
		// TODO this must be generalized and configurable
		if (nameParts.length == 5) {
			this.reportCategory = nameParts[0];
			this.origin = nameParts[1];
			this.organization = nameParts[2];
			this.year = nameParts[3];
		}
		// if the name conventions are not right //TODO must be editited if
		// formating of filenames is configurable
		else {
			this.reportCategory = null;
			this.origin = null;
			this.organization = null;
			this.year = null;
		}

		this.normalizedTokenCount = -1;
	}

	// public String getDocumentPath() {
	// return this.documentPath;
	// }

	/**
	 * Prints the results of the lemmatization. With the momentarily definition,
	 * the vector count is identical to the normalization paremter. The pieces
	 * of information printed include the file name, the the type of the report,
	 * the originating stock markte index, the enterprise, the year, the values
	 * of the vector itself, the lenght of the vector, the token count, the
	 * letter token count and the vector count (tokens mapped successfully to a
	 * lexicon entry).
	 */
	public void printData() {
		// i dont use this because it would not only give the identified tokens,
		// but all of them.
		// if (this.settings.printIdentifiedTokens){
		// System.out.println("\nIdentified Tokens:\n\n");
		// for (String token: this.bagOfWords){
		// System.out.println(token);
		// }
		// }
		System.out.println(this.document.getName() + "\t" + this.reportCategory
				+ "\t" + this.origin + "\t" + this.organization + "\t"
				+ this.year + "\t" + this.documentEmotionVector.getValence()
				+ "\t" + this.documentEmotionVector.getArousal() + "\t"
				+ this.documentEmotionVector.getDominance() + "\t"
				+ this.documentEmotionVector.getLength() + "\t"
				+ this.standardDeviationVector.getValence() + "\t"
				+ this.standardDeviationVector.getArousal() + "\t"
				+ this.standardDeviationVector.getDominance() + "\t"
				+ this.tokenCount + "\t" + this.normalizedTokenCount + "\t"
				+ this.recognizedTokenCount);
	}

	// public EmotionVector getNormalizedEmotionVector() {
	// return documentEmotionVector;
	// }

	// private EmotionVector sumOfVectors;

	/**
	 * Number of tokens in the input text. Tokenization is done be Stanfords
	 * PTBTokenizer.
	 */

	public int getTokenCount() {
		return tokenCount;
	}

	// private double normalizationParameter;

	public int getVectorCount() {
		return recognizedTokenCount;
	}

	public int[] getDocumentTermVector(int vocabularySize)
			throws NumberFormatException, IOException {
		int[] vector = new int[vocabularySize];
		BufferedReader reader = new BufferedReader(new FileReader(
				this.documentTermVector));
		String currentLine;
		int i = 0;
		while ((currentLine = reader.readLine()) != null) {
			vector[i++] = Integer.parseInt(currentLine);
		}
		reader.close();
		return vector;
	}

	// public double getNormalizationParameter() {
	// return normalizationParameter;
	// }

	// private HashMultiset<String> bagOfWords;

	// public HashMultiset<String> getBagOfWords() {
	// return bagOfWords;
	// }

	// public void setBagOfWords(HashMultiset<String> bagOfWords) {
	// this.bagOfWords = bagOfWords;
	// }

	// public void calculateBagOfWords(File2BagOfWords_Processor givenF2TReader)
	// throws IOException{
	// switch (this.usedPreprocessing){
	// case TOKENIZE:
	// this.bagOfWords =
	// givenF2TReader.produceBagOfWords_Token(this.documentPath);
	// break;
	// case LEMMATIZE:
	// this.bagOfWords =
	// givenF2TReader.produceBagOfWords_Lemma(this.documentPath);
	// break;
	// case STEM:
	// this.bagOfWords =
	// givenF2TReader.produceBagOfWords_Stems(this.documentPath);
	// }
	//
	// this.tokenCount = this.bagOfWords.size();
	// }
	//

	// /**
	// * Calculates the number of letter tokens ((Tokens which purely of letters
	// and can therefore be regarded as "real words". This deviation may be
	// important in this context to interprete the difference between token
	// count and count of identified tokens during look-up because especially in
	// annual reports, many tokens may be numbers.).
	// */
	// public void calculateLetterTokenCount(){
	// int letterTokenCount = 0;
	// for (String currentToken: this.bagOfWords){
	// if (Util.isLetterToken(currentToken)) letterTokenCount++;
	// }
	// // if (letterTokenCount==-1) Throw new
	// Exception("Error in calculation of letter token count!")
	// this.normalizedTokenCount = letterTokenCount;
	// }
	//
	// public void calculateSumOfVectors(BagOfWords2Vector_Processor
	// givenToken2Vectorizer, EmotionLexicon givenLexicon) throws IOException{
	// VectorizationResult result =
	// givenToken2Vectorizer.calculateDocumentVector(this.bagOfWords,
	// givenLexicon, this.settings); //calcualtes not normalized emotion vector
	// (sum of found vectors
	// this.sumOfVectors = result.getEmotionVector();
	// // this.sumOfVectors.print();
	// this.recognizedTokenCount = result.getNumberOfAddedVectors();
	// }

	// public void normalizeDocumentVector(VectorNormalizer
	// givenVectorNormalizer){
	// this.normalizationParameter =
	// givenVectorNormalizer.calculateNormalizationParameter(this.recognizedTokenCount);
	// this.normalizedEmotionVector =
	// givenVectorNormalizer.calculateNormalizedDocumentVector(this.sumOfVectors,
	// this.normalizationParameter);
	// }

	// public EmotionVector getSumOfVectors() {
	// return sumOfVectors;
	// }

}
