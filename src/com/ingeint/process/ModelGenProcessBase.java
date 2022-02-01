package com.ingeint.process;

import com.ingeint.base.CustomProcess;
import com.ingeint.model.MModelGenerator;

public abstract class ModelGenProcessBase extends CustomProcess{
	
	protected MModelGenerator m_mgen = null;
	public MModelGenerator getMMGen(){
		return m_mgen;
	}

	@Override
	protected String doIt() throws Exception {
		generate();
		addBufferLog(m_mgen.get_ID(), m_mgen.getCreated(),null,"@ModelGenerated@", m_mgen.get_Table_ID(),m_mgen.get_ID());
		return null;
	}
	
	abstract void generate();

	@Override
	protected void prepare() {
		m_mgen = new MModelGenerator(getCtx(), getRecord_ID(), get_TrxName());
	}

}

