package com.ingeint.process;

import com.ingeint.util.ModelGen;
import com.ingeint.util.ModelInterfaceGen;

public class ModelGenProcess extends ModelGenProcessBase {

	@Override
	void generate(String sourceFolder, String packageName, String entityType, String tableName) {
		if (!m_isCore) {
			ModelInterfaceGen.generateSource(sourceFolder, packageName, entityType, tableName, null);
		} 
		m_isBaseClass = true;
		ModelGen.generateSource(sourceFolder, packageName, tableName, entityType, entityType, this);	
	}
}
