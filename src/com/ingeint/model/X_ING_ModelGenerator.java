/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package com.ingeint.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.model.X_AD_Table;
import org.compiere.util.KeyNamePair;

/**
 * Generated Model for table ING_ModelGenerator
 * @author iDempiere (generated)
 * @version Release 8.2 - $Id$
 */
public class X_ING_ModelGenerator extends PO implements I_ING_ModelGenerator, I_Persistent {

	private static final long serialVersionUID = 20220203L;

	/**
	 * Standard Constructor
	 */
	public X_ING_ModelGenerator (Properties ctx, int ING_ModelGenerator_ID, String trxName) {
		super (ctx, ING_ModelGenerator_ID, trxName);
    }

	/**
	 * Load Constructor
	 */
	public X_ING_ModelGenerator (Properties ctx, ResultSet rs, String trxName) {
		super (ctx, rs, trxName);
	}

	/**
	 * AccessLevel
	 * @return 4 - System 
	 */
	protected int get_AccessLevel() {
		return accessLevel.intValue();
	}

	/**
	 * Load Meta Data
	 */
	protected POInfo initPO (Properties ctx) {
		POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
		return poi;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder ("X_ING_ModelGenerator[")
			.append(get_ID()).append("]");
		return sb.toString();
	}

	/**
	 * Set Base Class Package Name.
	 * @param BaseClassPackage 
	 * Name of Dictionary Baseclass Package
	 */
	public void setBaseClassPackage (String baseClassPackage) {
		set_Value (COLUMNNAME_BaseClassPackage, baseClassPackage);
	}

	/**
	 * Get Base Class Package Name.
	 * @return Name of Dictionary Baseclass Package
	 */
	public String getBaseClassPackage () {
		return (String)get_Value(COLUMNNAME_BaseClassPackage);
	}

	/** ColumnEntityTypeFilter AD_Reference_ID=389 */
	public static final int COLUMNENTITYTYPEFILTER_AD_Reference_ID=389;
	/**
	 * Set Column Entity Type Filter .
	 * @param ColumnEntityTypeFilter 
	 * A list of Entity Types.
	 */
	public void setColumnEntityTypeFilter (String columnEntityTypeFilter) {

		set_Value (COLUMNNAME_ColumnEntityTypeFilter, columnEntityTypeFilter);
	}

	/**
	 * Get Column Entity Type Filter .
	 * @return A list of Entity Types.
	 */
	public String getColumnEntityTypeFilter () {
		return (String)get_Value(COLUMNNAME_ColumnEntityTypeFilter);
	}

	/**
	 * Set Generate Model Class (Stub).
	 * @param CustomizableModelGenProcess 
	 * Generates the Model class (Stub).
	 */
	public void setCustomizableModelGenProcess (String customizableModelGenProcess) {
		set_Value (COLUMNNAME_CustomizableModelGenProcess, customizableModelGenProcess);
	}

	/**
	 * Get Generate Model Class (Stub).
	 * @return Generates the Model class (Stub).
	 */
	public String getCustomizableModelGenProcess () {
		return (String)get_Value(COLUMNNAME_CustomizableModelGenProcess);
	}

	/**
	 * Set Custom Prefix.
	 * @param CustomPrefix 
	 * Prefix for Custom entities
	 */
	public void setCustomPrefix (String customPrefix) {
		set_Value (COLUMNNAME_CustomPrefix, customPrefix);
	}

	/**
	 * Get Custom Prefix.
	 * @return Prefix for Custom entities
	 */
	public String getCustomPrefix () {
		return (String)get_Value(COLUMNNAME_CustomPrefix);
	}

	/**
	 * Set Folder.
	 * @param Folder 
	 * A folder on a local or remote system to store data into
	 */
	public void setFolder (String folder) {
		set_Value (COLUMNNAME_Folder, folder);
	}

	/**
	 * Get Folder.
	 * @return A folder on a local or remote system to store data into
	 */
	public String getFolder () {
		return (String)get_Value(COLUMNNAME_Folder);
	}

	/**
	 * Set Has Custom Columns.
	 * @param HasCustomColumns 
	 * Model generation is skipped if no custom columns are present
	 */
	public void setHasCustomColumns (boolean hasCustomColumns) {
		set_Value (COLUMNNAME_HasCustomColumns, Boolean.valueOf(hasCustomColumns));
	}

	/**
	 * Get Has Custom Columns.
	 * @return Model generation is skipped if no custom columns are present
	 */
	public boolean isHasCustomColumns () {
		Object oo = get_Value(COLUMNNAME_HasCustomColumns);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Comment/Help.
	 * @param Help 
	 * Comment or Hint
	 */
	public void setHelp (String help) {
		set_Value (COLUMNNAME_Help, help);
	}

	/**
	 * Get Comment/Help.
	 * @return Comment or Hint
	 */
	public String getHelp () {
		return (String)get_Value(COLUMNNAME_Help);
	}

	/**
	 * Set Model Generator.
	 * @param ING_ModelGenerator_ID Model Generator
	 */
	public void setING_ModelGenerator_ID (int modelGeneratorID) {
		if (modelGeneratorID < 1) 
			set_ValueNoCheck (COLUMNNAME_ING_ModelGenerator_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_ING_ModelGenerator_ID, Integer.valueOf(modelGeneratorID));
	}

	/**
	 * Get Model Generator.
	 * @return Model Generator
	 */
	public int getING_ModelGenerator_ID () {
		Integer ii = (Integer)get_Value(COLUMNNAME_ING_ModelGenerator_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/**
	 * Set ING_ModelGenerator_UU.
	 * @param ING_ModelGenerator_UU ING_ModelGenerator_UU
	 */
	public void setING_ModelGenerator_UU (String modelGeneratorUU) {
		set_Value (COLUMNNAME_ING_ModelGenerator_UU, modelGeneratorUU);
	}

	/**
	 * Get ING_ModelGenerator_UU.
	 * @return ING_ModelGenerator_UU
	 */
	public String getING_ModelGenerator_UU () {
		return (String)get_Value(COLUMNNAME_ING_ModelGenerator_UU);
	}

	public X_AD_Table getING_Table() throws RuntimeException {
		return (X_AD_Table)MTable.get(getCtx(), X_AD_Table.Table_Name)
				.getPO(getING_Table_ID(), get_TrxName());
	}

	/**
	 * Set ING_Table_ID.
	 * @param ING_Table_ID ING_Table_ID
	 */
	public void setING_Table_ID (int tableID) {
		if (tableID < 1) 
			set_Value (COLUMNNAME_ING_Table_ID, null);
		else 
			set_Value (COLUMNNAME_ING_Table_ID, Integer.valueOf(tableID));
	}

	/**
	 * Get ING_Table_ID.
	 * @return ING_Table_ID
	 */
	public int getING_Table_ID () {
		Integer ii = (Integer)get_Value(COLUMNNAME_ING_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/**
	 * Set Extension.
	 * @param IsExtension 
	 * Model is an extension of an existing (core) class
	 */
	public void setIsExtension (boolean isExtension) {
		set_Value (COLUMNNAME_IsExtension, Boolean.valueOf(isExtension));
	}

	/**
	 * Get Extension.
	 * @return Model is an extension of an existing (core) class
	 */
	public boolean isExtension () {
		Object oo = get_Value(COLUMNNAME_IsExtension);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/**
	 * Set Interface and Model Base Class.
	 * @param ModelGenProcess 
	 * Generates the interface and model base classes.
	 */
	public void setModelGenProcess (String modelGenProcess) {
		set_Value (COLUMNNAME_ModelGenProcess, modelGenProcess);
	}

	/**
	 * Get Interface and Model Base Class.
	 * @return Generates the interface and model base classes.
	 */
	public String getModelGenProcess () {
		return (String)get_Value(COLUMNNAME_ModelGenProcess);
	}

	/**
	 * Set Package Name.
	 * @param PackageName Package Name
	 */
	public void setPackageName (String packageName) {
		set_Value (COLUMNNAME_PackageName, packageName);
	}

	/**
	 * Get Package Name.
	 * @return Package Name
	 */
	public String getPackageName () {
		return (String)get_Value(COLUMNNAME_PackageName);
	}

	/** TableEntityTypeFilter AD_Reference_ID=389 */
	public static final int TABLEENTITYTYPEFILTER_AD_Reference_ID=389;
	/**
	 * Set Table Entity Type Filter .
	 * @param TableEntityTypeFilter 
	 * A list of Entity Types.
	 */
	public void setTableEntityTypeFilter (String tableEntityTypeFilter) {

		set_Value (COLUMNNAME_TableEntityTypeFilter, tableEntityTypeFilter);
	}

	/**
	 * Get Table Entity Type Filter .
	 * @return A list of Entity Types.
	 */
	public String getTableEntityTypeFilter () {
		return (String)get_Value(COLUMNNAME_TableEntityTypeFilter);
	}

	/**
	 * Set DB Table Name.
	 * @param TableName 
	 * Name of the table in the database
	 */
	public void setTableName (String tableName) {
		set_Value (COLUMNNAME_TableName, tableName);
	}

	/**
	 * Get DB Table Name.
	 * @return Name of the table in the database
	 */
	public String getTableName () {
		return (String)get_Value(COLUMNNAME_TableName);
	}

	/**
	 * Get Record ID/ColumnName
	 * @return ID/ColumnName pair
	 */
	public KeyNamePair getKeyNamePair() {
		return new KeyNamePair(get_ID(), getTableName());
	}
}