package com.ingeint.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MColumn;
import org.compiere.model.MEntityType;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MModelGenerator extends X_ING_ModelGenerator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1539402347529793581L;
	
	private static CLogger s_log = CLogger.getCLogger (MModelGenerator.class);


	public MModelGenerator(Properties ctx, int ING_ModelGenerator_ID, String trxName) {
		super(ctx, ING_ModelGenerator_ID, trxName);
	}
	
	public MModelGenerator(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**
	 * produces a MModelgenerator for the given tableID. If none exists it will be created but not saved. 
	 * The user of the object is responsible for persisting the object if so desired.
	 * @param tableID
	 * @param getNew - if true ignores existing DB entries and initializes mmgen from system settings.
	 * @param trxName
	 * @return
	 */
	public static MModelGenerator get(int tableID, boolean getNew, String trxName) {
		MModelGenerator gen;
		if (getNew)
			gen = getNew(tableID, trxName);
		else {
			String sql = "select * from ing_modelgenerator where ing_table_id = ?";
	        try (PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, trxName)){
	        	pstmt.setInt(1, tableID);
	        	try(ResultSet rs = pstmt.executeQuery()) {
		            if (rs.next()) {
		            	gen = new MModelGenerator(Env.getCtx(), rs, trxName);
		            	if (rs.next())
		            		gen.log.warning("found more than one item for tableID " + tableID);
		            } 
		            else {
		            	gen = getNew(tableID, trxName);
		            }
	        	}
	        }
	        catch (SQLException e) {
	        	throw new DBException(e, sql);
	        }
		}
    	return gen;
	}

	/**
	 * @param tableID
	 * @param trxName
	 * @return
	 */
	private static MModelGenerator getNew(int tableID, String trxName) {
		MModelGenerator gen = new MModelGenerator(Env.getCtx(), 0, trxName);
		gen.setING_Table_ID(tableID);
		gen.setTableName(MTable.getTableName(Env.getCtx(), tableID));
		
		gen.setFolder(MSysConfig.getValue("Default_Source_Folder"));

		String tableET = MTable.get(tableID).getEntityType();
		String defaultET = MSysConfig.getValue("DEFAULT_ENTITYTYPE");
		
		boolean isExtension = !tableET.equals(defaultET);
		gen.setIsExtension(isExtension);
		
		gen.setTableEntityTypeFilter(tableET);
		gen.setColumnEntityTypeFilter(defaultET);
		
		gen.setPackageName( MEntityType.get(defaultET).getModelPackage() );
		if (isExtension) {
			gen.setBaseClassPackage( "D".equals(tableET) ? "org.compiere.model" : MEntityType.get(tableET).getModelPackage() );
		}
		return gen;
	}
	
	public static int get_ID(int tableID, String trxName) {
		String sql = "select ing_modelgenerator_id from ing_modelgenerator where ing_table_id = ?";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, trxName)){
        	pstmt.setInt(1, tableID);
        	try(ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	            	int genID = rs.getInt(1);
	            	if (rs.next())
	            		s_log.warning("found more than one item for tableID " + tableID);
	            	if (genID > 0)
	            		return genID;
	            } 
        	}

        	MModelGenerator gen = getNew(tableID, trxName);
        	gen.saveEx();
        	return gen.get_ID();
        }
        catch (SQLException e) {
        	throw new DBException(e, sql);
        }
	}
	
	
	
	@Override
	public String getTableName() {
		if (getING_Table_ID()>0) {
			return new MTable(getCtx(), getING_Table_ID(), get_TrxName()).getTableName();
		}
		return super.getTableName();
	}

	public static MModelGenerator getFromTableID(int tableID) {
		return new Query(Env.getCtx(), MModelGenerator.Table_Name, "ing_table_id=?", null).setParameters(tableID).first();
	}

	public static MModelGenerator get(MColumn column) {
		return getFromTableID(column.getAD_Table_ID());
	}

}
