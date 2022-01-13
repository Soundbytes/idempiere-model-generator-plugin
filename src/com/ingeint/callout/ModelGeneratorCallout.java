package com.ingeint.callout;

import java.util.List;

import org.compiere.model.MTable;
import org.compiere.util.DB;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Util;
import org.zkoss.zul.Messagebox;

import com.ingeint.base.CustomCallout;
import com.ingeint.model.MModelGenerator;
import com.ingeint.util.ModelInterfaceGen;

public class  ModelGeneratorCallout extends CustomCallout {
	private Boolean m_IsCoreTable = null;
	
	@Override
	protected String start() {
		String tableName;
		Integer tableID;
		switch(getField().getColumnName()) {
		case MModelGenerator.COLUMNNAME_ING_Table_ID:
			tableID = (Integer)getValue();
			if(tableID == null || tableID < 1) {
				getTab().setValueSilent(MModelGenerator.COLUMNNAME_TableName, null);
				getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, null);
			} else {
				MTable table = new MTable(getCtx(),tableID,null);
				getTab().setValueSilent(MModelGenerator.COLUMNNAME_TableName, table.getTableName());
				if(isCoreTable()) {
					String packageName = ModelInterfaceGen.getModelPackage(table.getEntityType());
					getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, packageName);
				}
			}
			break;
		case MModelGenerator.COLUMNNAME_TableName:
			tableName = (String)getValue();
			if (tableName == null || tableName.isBlank()) {
				getTab().setValueSilent(MModelGenerator.COLUMNNAME_ING_Table_ID,null);
				getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, null);
			} else {
				startTableName(tableName);
			}
			break;
		case MModelGenerator.COLUMNNAME_IsCoreTable:
			if (!isCoreTable()) {
				getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, null);
				break;
			}
			tableName = (String)getTab().getValue(MModelGenerator.COLUMNNAME_TableName);
			tableID = (Integer)getTab().getValue(MModelGenerator.COLUMNNAME_ING_Table_ID);
			if (tableID != null && tableID > 0){
				startTableID(tableID);
			} else if (tableName != null && !tableName.isBlank()) {
				startTableName(tableName);
			} else
				getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, null);
			break;
		}
		return null;
	}


	private void startTableID(Integer tableID) {
		MTable table = new MTable(getCtx(),tableID,null);
		getTab().setValueSilent(MModelGenerator.COLUMNNAME_TableName, table.getTableName());
		if(isCoreTable()) {
			String packageName = ModelInterfaceGen.getModelPackage(table.getEntityType());
			getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, packageName);
		}		
	}

	private void startTableName(String tableName) {
		String sql = "select ad_table_id,entitytype from ad_table where tablename like ?";
		KeyNamePair[] list = DB.getKeyNamePairs(sql, false, tableName);
		if (list.length == 1) {
			getTab().setValueSilent(MModelGenerator.COLUMNNAME_ING_Table_ID, list[0].getKey());
			if(isCoreTable())
				getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, 
						ModelInterfaceGen.getModelPackage(list[0].getName()));
		} else if (list.length > 1) {
			getTab().setValue(MModelGenerator.COLUMNNAME_ING_Table_ID, null);
			if(isCoreTable()) {
				String entitytype = list[0].getName();
				for (int i = 1; i<list.length;++i) {
					if (!entitytype.equals(list[i].getName())) {
						getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, null);
						break;
					}
				}
				getTab().setValue(MModelGenerator.COLUMNNAME_BaseClassPackage, 
						ModelInterfaceGen.getModelPackage(entitytype));
			}
		} else
			Messagebox.show("No table found for the filter '" + tableName + "'. Did you forget a wildcard '%' ?");
	}
	
	
	private boolean isCoreTable() {
		if (m_IsCoreTable == null)
			m_IsCoreTable = getTab().getValueAsBoolean(MModelGenerator.COLUMNNAME_IsCoreTable);
		return m_IsCoreTable;
	}

}

