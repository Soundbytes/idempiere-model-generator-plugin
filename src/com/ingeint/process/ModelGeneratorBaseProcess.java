package com.ingeint.process;

import com.ingeint.base.CustomProcess;
import com.ingeint.model.MModelGenerator;

abstract class ModelGeneratorBaseProcess extends CustomProcess{
	@Override
	protected String doIt() throws Exception {
		
		MModelGenerator mgen = new MModelGenerator(getCtx(), getRecord_ID(), get_TrxName());
		
		generate(mgen.getFolder(), 
				mgen.getPackageName(), 
				mgen.getEntityTypeFilter(), 
				mgen.getTableName());
		
		addBufferLog(mgen.get_ID(), mgen.getCreated(),null,"@ModelGenerated@", mgen.get_Table_ID(),mgen.get_ID());
		return null;
	}
	
	abstract void generate(String sourceFolder, String packageName, String entityType, String tableName);

	@Override
	protected void prepare() {}
}

