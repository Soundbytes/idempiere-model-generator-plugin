package com.ingeint.process;

import com.ingeint.util.ModelGen;
import com.ingeint.util.ModelInterfaceGen;

public class ModelGenProcess extends ModelGenProcessBase {

	@Override
	void generate(String sourceFolder, String packageName, String tableEntityType, String columnEntityType, String tableName) {
		if (!m_isCore) {
			ModelInterfaceGen.generateSource(sourceFolder, packageName, columnEntityType, tableName, null);
		} 
		m_isBaseClass = true;
		ModelGen.generateSource(sourceFolder, packageName, tableEntityType, columnEntityType, tableName, this);	
	}
}
