package com.ingeint.process;

import com.ingeint.util.MModelClassGenerator;

public class MModelGeneratorProcess extends ModelGeneratorBaseProcess {
	@Override
	void generate(String sourceFolder, String packageName, String entityType, String tableName) {
		MModelClassGenerator.generateSource(sourceFolder, packageName, entityType, tableName);	
	}
}

