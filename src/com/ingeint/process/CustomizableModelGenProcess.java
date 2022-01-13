package com.ingeint.process;

import com.ingeint.util.ModelGen;

public class CustomizableModelGenProcess extends ModelGenProcessBase {

	@Override
	void generate(String sourceFolder, String packageName, String tableEntityType, String columnEntityType, String tableName) {
		m_isBaseClass = false;
		ModelGen.generateSource(sourceFolder, packageName, tableEntityType, columnEntityType, tableName, this);	
	}
}

