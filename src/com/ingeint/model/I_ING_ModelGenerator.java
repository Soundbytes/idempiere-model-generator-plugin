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
import org.compiere.model.*;
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
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

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

    /** Column name EntityTypeFilter */
    public static final String COLUMNNAME_EntityTypeFilter = "EntityTypeFilter";

	/** Set Entity Type Filter .
	  * A list of Entity Types.
	  */
	public void setEntityTypeFilter (String EntityTypeFilter);

	/** Get Entity Type Filter .
	  * A list of Entity Types.
	  */
	public String getEntityTypeFilter();

    /** Column name Folder */
    public static final String COLUMNNAME_Folder = "Folder";

	/** Set Folder.
	  * A folder on a local or remote system to store data into
	  */
	public void setFolder (String Folder);

	/** Get Folder.
	  * A folder on a local or remote system to store data into
	  */
	public String getFolder();

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name ING_ModelGenerator_ID */
    public static final String COLUMNNAME_ING_ModelGenerator_ID = "ING_ModelGenerator_ID";

	/** Set Model Generator	  */
	public void setING_ModelGenerator_ID (int ING_ModelGenerator_ID);

	/** Get Model Generator	  */
	public int getING_ModelGenerator_ID();

    /** Column name ING_ModelGenerator_UU */
    public static final String COLUMNNAME_ING_ModelGenerator_UU = "ING_ModelGenerator_UU";

	/** Set ING_ModelGenerator_UU	  */
	public void setING_ModelGenerator_UU (String ING_ModelGenerator_UU);

	/** Get ING_ModelGenerator_UU	  */
	public String getING_ModelGenerator_UU();

    /** Column name ING_Table_ID */
    public static final String COLUMNNAME_ING_Table_ID = "ING_Table_ID";

	/** Set ING_Table_ID	  */
	public void setING_Table_ID (int ING_Table_ID);

	/** Get ING_Table_ID	  */
	public int getING_Table_ID();

	public org.compiere.model.I_AD_Table getING_Table() throws RuntimeException;

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name MModelGeneratorProcess */
    public static final String COLUMNNAME_MModelGeneratorProcess = "MModelGeneratorProcess";

	/** Set Generate Model Class (Stub).
	  * Generates the Model class (Stub).
	  */
	public void setMModelGeneratorProcess (String MModelGeneratorProcess);

	/** Get Generate Model Class (Stub).
	  * Generates the Model class (Stub).
	  */
	public String getMModelGeneratorProcess();

    /** Column name ModelGeneratorProcess */
    public static final String COLUMNNAME_ModelGeneratorProcess = "ModelGeneratorProcess";

	/** Set Interface and Model Base Class.
	  * Generates the interface and model base classes.
	  */
	public void setModelGeneratorProcess (String ModelGeneratorProcess);

	/** Get Interface and Model Base Class.
	  * Generates the interface and model base classes.
	  */
	public String getModelGeneratorProcess();

    /** Column name PackageName */
    public static final String COLUMNNAME_PackageName = "PackageName";

	/** Set Package Name	  */
	public void setPackageName (String PackageName);

	/** Get Package Name	  */
	public String getPackageName();

    /** Column name TableName */
    public static final String COLUMNNAME_TableName = "TableName";

	/** Set DB Table Name.
	  * Name of the table in the database
	  */
	public void setTableName (String TableName);

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
