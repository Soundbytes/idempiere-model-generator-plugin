package com.ingeint.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;

import com.ingeint.model.MModelGenerator;

public class ModelGenMenu2Process extends ModelGenProcess {

	private String baseClassPackage;
	private String ColumnEntityTypeFilter;
	private String customPrefix;
	private String folder;
	private boolean hasCustomColumns;
	private int tableID;
	private boolean isExtension;
	private String packageName;
	private String tableEntityTypeFilter;

	@Override
	protected void prepare() {
		m_mgen = MModelGenerator.get(getRecord_ID(), false, get_TrxName());
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
			case "IsExtension":
				isExtension = "Y".equals(para[i].getParameter());
				break;
			case "PackageName":
				packageName = (String)para[i].getParameter();
				break;
			case "TableEntityTypeFilter":
				tableEntityTypeFilter = (String)para[i].getParameter();
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
		m_mgen.setIsExtension(isExtension);
		m_mgen.setPackageName(packageName);
		m_mgen.setTableEntityTypeFilter(tableEntityTypeFilter);
		m_mgen.saveEx();
	}


	

}
