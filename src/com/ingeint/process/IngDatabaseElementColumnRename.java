package com.ingeint.process;



import java.util.List;

import org.compiere.model.Query;
import org.compiere.process.DatabaseElementColumnRename;

import com.ingeint.model.MModelGenerator;

public class IngDatabaseElementColumnRename extends DatabaseElementColumnRename {
	@Override
	protected String doIt() throws Exception {
		String retVal = super.doIt();
		int elementID = getRecord_ID();
		String where = "ing_table_id in ("
				+ "select distinct c.ad_table_id from ad_column c "
				+ "where c.ad_element_id = ? "
				+ "group by c.ad_table_id)";
		List<MModelGenerator> modelGen = new Query(getCtx(), MModelGenerator.Table_Name, where, get_TrxName())
				.setParameters(elementID).list();
		for(MModelGenerator mg : modelGen) {
			mg.setIsDirty(true);
			mg.saveEx();
		}
		return retVal;
	}


}
