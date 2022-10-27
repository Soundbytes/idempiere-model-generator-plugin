package com.ingeint.process;

import org.compiere.model.MColumn;
import org.compiere.process.ColumnSync;

import com.ingeint.model.MModelGenerator;

public class IngColumnSync extends ColumnSync {
	protected String doIt() throws Exception {
		String retVal = super.doIt();

		int tableID = new MColumn(getCtx(), getRecord_ID(), get_TrxName()).getAD_Table_ID();
		MModelGenerator mg = MModelGenerator.getFromTableID(tableID);
		if (mg != null) {
			mg.setIsDirty(true);
			mg.saveEx();
		}
		return retVal;
	}
}
