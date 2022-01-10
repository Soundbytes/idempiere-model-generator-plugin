package com.ingeint.process;

import com.ingeint.util.ModelGen;

public class CustomizableModelGenProcess extends ModelGenProcessBase {

	@Override
	void generate(String sourceFolder, String packageName, String entityType, String tableName) {
		m_isBaseClass = false;
		ModelGen.generateSource(sourceFolder, packageName, tableName, entityType, entityType, this);	
	}
}

