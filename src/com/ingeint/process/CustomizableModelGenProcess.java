package com.ingeint.process;

import com.ingeint.util.ModelGen;

public class CustomizableModelGenProcess extends ModelGenProcessBase {

	@Override
	void generate() {
		ModelGen.generateSource(m_mgen, false);	
	}
}

