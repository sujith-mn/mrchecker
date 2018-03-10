package com.capgemini.kabanos.gatherer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.capgemini.kabanos.common.domain.Preposition;
import com.capgemini.kabanos.common.domain.TestFile;
import com.capgemini.kabanos.common.enums.TestFrameworkType;
import com.capgemini.kabanos.common.utility.ArrayUtils;
import com.capgemini.kabanos.common.utility.FileUtils;
import com.capgemini.kabanos.database.DataBase;
import com.capgemini.kabanos.gatherer.extractor.CucumberExtractor;
import com.capgemini.kabanos.gatherer.extractor.IExtractor;
import com.capgemini.kabanos.gatherer.extractor.JUnitExtractor;

public class Gatherer {

	// parallel processing not always gives benefit. if the array is to small then
	// parallel is not needed
	private final int PARALLEL_ARRAY_SIZE_THRESHOLD = 10;
	private IExtractor extractor = null;

	private Properties properties;

	public Gatherer(Properties properties) {
		this.properties = properties;
	}

	public List<Preposition> gatherKnowledge(TestFrameworkType frameworkType, String[] paths) throws Exception {

		switch (frameworkType) {
		case JUNIT:
			extractor = new JUnitExtractor(this.properties);
			break;
		case CUCUMBER:
			extractor = new CucumberExtractor();
			break;
		default:
			throw new Exception("Unsuported framework type: " + frameworkType);
		}

		Set<String> uniquePaths = new HashSet<>();
		for (String path : paths)
			uniquePaths.addAll(FileUtils.listFiles(path));

		List<TestFile> testFiles = uniquePaths.stream().filter(path -> path.endsWith(frameworkType.getFileType()))
				.map(filePath -> extractor.extractFunctionsFromTestFile(filePath)).collect(Collectors.toList());

		return createImplementationPrepositionList(testFiles);
	}

	/*
	 * list of prepositions in a list of tests
	 */
	private List<Preposition> createImplementationPrepositionList(List<TestFile> testFiles) {
		Stream<TestFile> stream = testFiles.size() > PARALLEL_ARRAY_SIZE_THRESHOLD ? testFiles.parallelStream()
				: testFiles.stream();

		return stream.map(this::extractPrepositionsFromTestFile).reduce(ArrayUtils::megre).orElse(new ArrayList<>());
	}

	private List<Preposition> extractPrepositionsFromTestFile(TestFile file) {
		return file.getTests().stream().map(extractor::gatherKnowledgeFromTest).filter(el -> el.size() > 0)
				.reduce((l, r) -> {
					l.addAll(r);
					return l;
				}).get();
	}

	public boolean saveKnowledge(List<Preposition> knowledge) {
		boolean result = false;
		DataBase db = null;
		try {
			db = new DataBase(this.properties);
			result = db.savePrepositions(knowledge);
		} finally {
			if (db != null)
				db.shutdown();
		}

		return result;
	}
}