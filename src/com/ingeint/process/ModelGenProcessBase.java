package com.ingeint.process;

import org.compiere.util.Util;

import com.ingeint.base.CustomProcess;
import com.ingeint.model.MModelGenerator;

public abstract class ModelGenProcessBase extends CustomProcess{
	
	protected String m_prefixBase = null;
	protected String m_prefixDerived = null;
	protected String m_baseClassPackage = null;
	protected boolean m_isCore = true;
	protected boolean m_isBaseClass;
	protected MModelGenerator m_mgen = null;
	
	public String getPrefixBase() {
		return m_prefixBase;
	}
	public String getPrefixDerived(){
		return m_prefixDerived;
	}
	public boolean isCoreTable(){
		return m_isCore;
	}
	
	public String getBaseClassPackage(){
		return m_baseClassPackage;
	}
	
	public MModelGenerator getMMGen(){
		return m_mgen;
	}

	public boolean isBaseClass() {
		return m_isBaseClass;
	}
	@Override
	protected String doIt() throws Exception {
		
		m_mgen = new MModelGenerator(getCtx(), getRecord_ID(), get_TrxName());
		init(m_mgen.getTableName(),
				m_mgen.isCoreTable(),
				m_mgen.getCustomPrefix(), 
				m_mgen.getBaseClassPackage());
		
		generate(m_mgen.getFolder(), 
				m_mgen.getPackageName(), 
				m_mgen.getTableEntityTypeFilter(), 
				m_mgen.getColumnEntityTypeFilter(), 
				m_mgen.getTableName());
		
		addBufferLog(m_mgen.get_ID(), m_mgen.getCreated(),null,"@ModelGenerated@", m_mgen.get_Table_ID(),m_mgen.get_ID());
		return null;
	}
	
	abstract void generate(String sourceFolder, String packageName,	String tableEntityType, String columnEntityType, String tableName);

	@Override
	protected void prepare() {}

	public void init(String tableName, boolean isCoreTable, String customPrefix, String baseClassPackage) {
		m_isCore = isCoreTable;
		if (isCoreTable) {
			if (customPrefix == null || Util.isEmpty(customPrefix)) 
				throw new IllegalArgumentException("Custom Prefix required for Core Table");
			if (baseClassPackage == null || Util.isEmpty(baseClassPackage)) 
				throw new IllegalArgumentException("Base Class Name required for Core Table");

			
			m_prefixBase = new StringBuilder("X").append(customPrefix).append("_").toString();
			m_prefixDerived = customPrefix;
			m_baseClassPackage  = baseClassPackage;
		} else {
			m_prefixBase = "X_";
			m_prefixDerived = "M";
			m_baseClassPackage = null;
		}
	}
}

