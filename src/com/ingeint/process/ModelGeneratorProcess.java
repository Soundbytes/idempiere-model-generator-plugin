package com.ingeint.process;

import org.adempiere.util.ModelClassGenerator;
import org.adempiere.util.ModelInterfaceGenerator;

public class ModelGeneratorProcess extends ModelGeneratorBaseProcess {
	@Override
	void generate(String sourceFolder, String packageName, String entityType, String tableName) {
		ModelInterfaceGenerator.generateSource(sourceFolder, packageName, entityType, tableName, null);
		ModelClassGenerator.generateSource(sourceFolder, packageName, entityType, tableName, null);	
	}
}
