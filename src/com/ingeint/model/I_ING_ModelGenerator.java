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
package com.ingeint.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.I_AD_Table;
import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

/** Generated Interface for ING_ModelGenerator
 *  @author iDempiere (generated) 
 *  @version Release 8.2
 */
@SuppressWarnings("all")
public interface I_ING_ModelGenerator 
{

    /** TableName=ING_ModelGenerator */
    public static final String Table_Name = "ING_ModelGenerator";

    /** AD_Table_ID=1000016 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 4 - System 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(4);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int orgID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name BaseClassPackage */
    public static final String COLUMNNAME_BaseClassPackage = "BaseClassPackage";

	/** Set Base Class Package Name.
	  * Name of Dictionary Baseclass Package
	  */
	public void setBaseClassPackage (String baseClassPackage);

	/** Get Base Class Package Name.
	  * Name of Dictionary Baseclass Package
	  */
	public String getBaseClassPackage();

    /** Column name ColumnEntityTypeFilter */
    public static final String COLUMNNAME_ColumnEntityTypeFilter = "ColumnEntityTypeFilter";

	/** Set Column Entity Type Filter .
	  * A list of Entity Types.
	  */
	public void setColumnEntityTypeFilter (String columnEntityTypeFilter);

	/** Get Column Entity Type Filter .
	  * A list of Entity Types.
	  */
	public String getColumnEntityTypeFilter();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name CustomizableModelGenProcess */
    public static final String COLUMNNAME_CustomizableModelGenProcess = "CustomizableModelGenProcess";

	/** Set Generate Model Class (Stub).
	  * Generates the Model class (Stub).
	  */
	public void setCustomizableModelGenProcess (String customizableModelGenProcess);

	/** Get Generate Model Class (Stub).
	  * Generates the Model class (Stub).
	  */
	public String getCustomizableModelGenProcess();

    /** Column name CustomPrefix */
    public static final String COLUMNNAME_CustomPrefix = "CustomPrefix";

	/** Set Custom Prefix.
	  * Prefix for Custom entities
	  */
	public void setCustomPrefix (String customPrefix);

	/** Get Custom Prefix.
	  * Prefix for Custom entities
	  */
	public String getCustomPrefix();

    /** Column name Folder */
    public static final String COLUMNNAME_Folder = "Folder";

	/** Set Folder.
	  * A folder on a local or remote system to store data into
	  */
	public void setFolder (String folder);

	/** Get Folder.
	  * A folder on a local or remote system to store data into
	  */
	public String getFolder();

    /** Column name HasCustomColumns */
    public static final String COLUMNNAME_HasCustomColumns = "HasCustomColumns";

	/** Set Has Custom Columns.
	  * Model generation is skipped if no custom columns are present
	  */
	public void setHasCustomColumns (boolean hasCustomColumns);

	/** Get Has Custom Columns.
	  * Model generation is skipped if no custom columns are present
	  */
	public boolean isHasCustomColumns();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name ING_ModelGenerator_ID */
    public static final String COLUMNNAME_ING_ModelGenerator_ID = "ING_ModelGenerator_ID";

	/** Set Model Generator	  */
	public void setING_ModelGenerator_ID (int modelGeneratorID);

	/** Get Model Generator	  */
	public int getING_ModelGenerator_ID();

    /** Column name ING_ModelGenerator_UU */
    public static final String COLUMNNAME_ING_ModelGenerator_UU = "ING_ModelGenerator_UU";

	/** Set ING_ModelGenerator_UU	  */
	public void setING_ModelGenerator_UU (String modelGeneratorUU);

	/** Get ING_ModelGenerator_UU	  */
	public String getING_ModelGenerator_UU();

    /** Column name ING_Table_ID */
    public static final String COLUMNNAME_ING_Table_ID = "ING_Table_ID";

	/** Set ING_Table_ID	  */
	public void setING_Table_ID (int tableID);

	/** Get ING_Table_ID	  */
	public int getING_Table_ID();

	public I_AD_Table getING_Table() throws RuntimeException;

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean isActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsCoreTable */
    public static final String COLUMNNAME_IsCoreTable = "IsCoreTable";

	/** Set Core Table.
	  * Table is in iDempiereCore (Not a custom Table)
	  */
	public void setIsCoreTable (boolean isCoreTable);

	/** Get Core Table.
	  * Table is in iDempiereCore (Not a custom Table)
	  */
	public boolean isCoreTable();

    /** Column name ModelGenProcess */
    public static final String COLUMNNAME_ModelGenProcess = "ModelGenProcess";

	/** Set Interface and Model Base Class.
	  * Generates the interface and model base classes.
	  */
	public void setModelGenProcess (String modelGenProcess);

	/** Get Interface and Model Base Class.
	  * Generates the interface and model base classes.
	  */
	public String getModelGenProcess();

    /** Column name PackageName */
    public static final String COLUMNNAME_PackageName = "PackageName";

	/** Set Package Name	  */
	public void setPackageName (String packageName);

	/** Get Package Name	  */
	public String getPackageName();

    /** Column name TableEntityTypeFilter */
    public static final String COLUMNNAME_TableEntityTypeFilter = "TableEntityTypeFilter";

	/** Set Table Entity Type Filter .
	  * A list of Entity Types.
	  */
	public void setTableEntityTypeFilter (String tableEntityTypeFilter);

	/** Get Table Entity Type Filter .
	  * A list of Entity Types.
	  */
	public String getTableEntityTypeFilter();

    /** Column name TableName */
    public static final String COLUMNNAME_TableName = "TableName";

	/** Set DB Table Name.
	  * Name of the table in the database
	  */
	public void setTableName (String tableName);

	/** Get DB Table Name.
	  * Name of the table in the database
	  */
	public String getTableName();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
