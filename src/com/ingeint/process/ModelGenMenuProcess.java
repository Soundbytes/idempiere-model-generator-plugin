package com.ingeint.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;

import com.ingeint.model.MModelGenerator;
import com.ingeint.util.ModelGen;
import com.ingeint.util.ModelInterfaceGen;

public class ModelGenMenuProcess extends ModelGenProcess {
	
	public ModelGenMenuProcess() {
		m_mgen = MModelGenerator.get(getRecord_ID(), get_TrxName());
	}


	private boolean createBase;
	private boolean createCustom;

	@Override
	protected void prepare() {
		String baseClassPackage = null;
		String ColumnEntityTypeFilter = null;
		String customPrefix = null;
		String folder = null;
		boolean hasCustomColumns = true;
		int tableID = 0;
		boolean isCoreTable = false;
		String packageName = null;
		String tableEntityTypeFilter = null;		
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++) {
			String name = para[i].getParameterName();
			switch (name) {
			case "BaseClassPackage":
				baseClassPackage = (String)para[i].getParameter();
				break;
			case "ColumnEntityTypeFilter":
				ColumnEntityTypeFilter = (String)para[i].getParameter();
				break;
			case "CustomPrefix":
				customPrefix = (String)para[i].getParameter(); 
				break;
			case "Folder":
				folder = (String)para[i].getParameter(); 
				break;
			case "HasCustomColumns":
				hasCustomColumns = "Y".equals(para[i].getParameter());
				break;
			case "AD_Table_ID":
				tableID = para[i].getParameterAsInt();
				break;
			case "IsCoreTable":
				isCoreTable = "Y".equals(para[i].getParameter());
				break;
			case "PackageName":
				packageName = (String)para[i].getParameter();
				break;
			case "TableEntityTypeFilter":
				tableEntityTypeFilter = (String)para[i].getParameter();
				break;
			case "CreateBase":
				createBase = "Y".equals(para[i].getParameter());
				break;
			case "CreateCustom":
				createCustom = "Y".equals(para[i].getParameter());
				break;
			default:
				log.log(Level.SEVERE, "Unknown Parameter: " + name);		
			}
		}
		m_mgen.setBaseClassPackage(baseClassPackage);
		m_mgen.setColumnEntityTypeFilter(ColumnEntityTypeFilter);
		m_mgen.setCustomPrefix(customPrefix);
		m_mgen.setFolder(folder);
		m_mgen.setHasCustomColumns(hasCustomColumns);
		m_mgen.setING_Table_ID(tableID);
		m_mgen.setIsCoreTable(isCoreTable);
		m_mgen.setPackageName(packageName);
		m_mgen.setTableEntityTypeFilter(tableEntityTypeFilter);
		m_mgen.saveEx();
	}

	@Override
	void generate() {
		if (createBase) {
			if (!m_mgen.isCoreTable())
				ModelInterfaceGen.generateSource(m_mgen);
			ModelGen.generateSource(m_mgen, true);	
		}
		if (createCustom) {
			ModelGen.generateSource(m_mgen, false);	
		}
	}
	

}
