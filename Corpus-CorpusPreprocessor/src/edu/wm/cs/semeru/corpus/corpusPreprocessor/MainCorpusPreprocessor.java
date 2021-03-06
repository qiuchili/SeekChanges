package edu.wm.cs.semeru.corpus.corpusPreprocessor;

public class MainCorpusPreprocessor
{
	static void testjEdit() throws Exception
	{
		String inputFileNameCorpus="TestCases/Input/Corpus-jEdit4.3.corpusRawMethodLevelGranularity";
		String outputFolder="TestCases/Output/";
		
		CorpusPreprocessor corpusPreprocessor=new CorpusPreprocessor(inputFileNameCorpus,outputFolder);
		corpusPreprocessor.preprocessCorpus();
	}
	
	static void testSystem1() throws Exception
	{
		String inputFileNameCorpus="TestCases/Input/Corpus-System1.corpusRawMethodLevelGranularity";
		String outputFolder="TestCases/Output/";
		
		CorpusPreprocessor corpusPreprocessor=new CorpusPreprocessor(inputFileNameCorpus,outputFolder);
		corpusPreprocessor.preprocessCorpus();
	}
	
	static void testSystem2() throws Exception
	{
		String inputFileNameCorpus="TestCases/Input/Corpus-System2.corpusRawMethodLevelGranularity";
		String outputFolder="TestCases/Output/";
		
		CorpusPreprocessor corpusPreprocessor=new CorpusPreprocessor(inputFileNameCorpus,outputFolder);
		corpusPreprocessor.preprocessCorpus();
	}
	
	static void testSystem3() throws Exception
	{
		String inputFileNameCorpus="TestCases/Input/Corpus-System3.corpusRawMethodLevelGranularity";
		String outputFolder="TestCases/Output/";
		
		CorpusPreprocessor corpusPreprocessor=new CorpusPreprocessor(inputFileNameCorpus,outputFolder);
		corpusPreprocessor.preprocessCorpus();
	}

	public static void main(String[] args) throws Exception
	{
		testjEdit();
		testSystem1();
		testSystem2();
		testSystem3();
		if (1==1)
			return;
//		
//		args=new String[2];
//		args[0]="TestCases/Input/Corpus-jEdit4.3.corpusRawMethodLevelGranularity";
//		args[1]="TestCases/Output/";
		
		if (args.length!=2)
		{
			System.err.println("Preprocesses (i.e., removes non-literals, splits identifiers, eliminates stop words and stems) a corpus generated by CorpusGenerator");
			System.err.println("Usage:");
			System.err.println("  java -jar CorpusPreprocessor.jar inputFileNameCorpus outputFolder");
			System.err.println();
			System.err.println("Where:");
			System.err.println("  inputFileNameCorpus");
			System.err.println("    is a file name containing the corpus. Each line represents a document. The corpus can be at method, file or class level granularity. This file should be the one with the extension .corpusRaw[File/Method/Class]LevelGranularity produced by CorpusGenerator");
			System.err.println("  outputFolder");
			System.err.println("    is the folder name where the preprocessed corpus will be saved");
			System.err.println();
			System.err.println("The output produced by this tool will contain the following files (assuming the input file name is Corpus-System.[extension]):");
			System.err.println("  Corpus-System"+CorpusPreprocessor.SUFFIX_AFTER_SPLIT+".[extension]");
			System.err.println("    contains the corpus after eliminating non-literals (e.g., special characters) and after splitting the identifiers using the CamelCase algorithm");
			System.err.println("  Corpus-System"+CorpusPreprocessor.SUFFIX_AFTER_SPLIT_STOP+".[extension]");
			System.err.println("    contains the corpus after eliminating non-literals, after splitting, and after eliminating stop words");
			System.err.println("  Corpus-System"+CorpusPreprocessor.SUFFIX_AFTER_SPLIT_STOP_STEM+".[extension]");
			System.err.println("    contains the corpus after eliminating non-literals, after splitting, after eliminating stop words and after stemming using the Porter stemmer. This file contains the final version of the preprocessed corpus");
			System.err.println();
			System.err.println("Example:");
			System.err.println("  java -jar CorpusPreprocessor.jar TestCases/Input/Corpus-jEdit4.3.corpusRawMethodLevelGranularity TestCases/Output/");
			System.exit(1);
		}
		
		CorpusPreprocessor corpusPreprocessor=new CorpusPreprocessor(args[0],args[1]);
		corpusPreprocessor.preprocessCorpus();
	}
}
