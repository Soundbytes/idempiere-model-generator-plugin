package com.ingeint.process;

import com.ingeint.util.ModelGen;
import com.ingeint.util.ModelInterfaceGen;

public class ModelGenProcess extends ModelGenProcessBase {

	@Override
	void generate() {
		if (m_mgen.isExtension()) {
			ModelInterfaceGen.generateSource(m_mgen);
		} 
		ModelGen.generateSource(m_mgen, true);	
	}
}
