package com.ingeint.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.model.MTable;

import com.ingeint.model.X_ING_ModelGenerator;

public class MModelGenerator extends X_ING_ModelGenerator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1539402347529793581L;

	public MModelGenerator(Properties ctx, int ING_ModelGenerator_ID, String trxName) {
		super(ctx, ING_ModelGenerator_ID, trxName);
		// TODO Auto-generated constructor stub
	}
	
	public MModelGenerator(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getTableName() {
		if (getING_Table_ID()>0) {
			return new MTable(getCtx(), getING_Table_ID(), get_TrxName()).getTableName();
		}
		return super.getTableName();
	}

}
