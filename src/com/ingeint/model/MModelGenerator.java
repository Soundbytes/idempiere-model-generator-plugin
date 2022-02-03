package com.ingeint.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.adempiere.exceptions.DBException;
import org.compiere.model.MEntityType;
import org.compiere.model.MSysConfig;
import org.compiere.model.MTable;
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
	 * @param trxName
	 * @return
	 */
	public static MModelGenerator get(int tableID, String trxName) {
		String sql = "select * from ing_modelgenerator where ing_table_id = ?";
        try (PreparedStatement pstmt = DB.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, trxName)){
        	pstmt.setInt(1, tableID);
        	try(ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	            	MModelGenerator gen = new MModelGenerator(Env.getCtx(), rs, trxName);
	            	if (rs.next())
	            		gen.log.warning("found more than one item for tableID " + tableID);
	            	return gen;
	            } 
	            else {
	            	return getNew(tableID, trxName);
	            }
        	}
        }
        catch (SQLException e) {
        	throw new DBException(e, sql);
        }
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
		String et = MTable.get(tableID).getEntityType();
		gen.setTableEntityTypeFilter(et);
		gen.setPackageName( MEntityType.get(et).getModelPackage() );
		gen.setFolder(MSysConfig.getValue("Default_Source_Folder"));
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

}
