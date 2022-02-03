/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 * Contributor(s): Carlos Ruiz - globalqss                                    *
 *                 Teo Sarca - www.arhipac.ro                                 *
 *                 Trifon Trifonov                                            *
 *****************************************************************************/
package com.ingeint.util;

import static org.compiere.model.SystemIDs.REFERENCE_PAYMENTRULE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.DBException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Messagebox;
import org.compiere.Adempiere;
import org.compiere.model.MTable;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Util;

import com.ingeint.model.MModelGenerator;

/**
 * Generate Model Classes extending PO.
 * Base class for CMP interface - will be extended to create byte code directly
 *
 * @author Jorg Janke
 * @version $Id: GenerateModel.java,v 1.42 2005/05/08 15:16:56 jjanke Exp $
 *
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 * 				<li>BF [ 1781629 ] Don't use Env.NL in model class/interface generators
 * 				<li>FR [ 1781630 ] Generated class/interfaces have a lot of unused imports
 * 				<li>BF [ 1781632 ] Generated class/interfaces should be UTF-8
 * 				<li>FR [ xxxxxxx ] better formating of generated source
 * 				<li>FR [ 1787876 ] ModelClassGenerator: list constants should be ordered
 * 				<li>FR [ 1803309 ] Model generator: generate get method for Search cols
 * 				<li>FR [ 1990848 ] Generated Models: remove hardcoded field length
 * 				<li>FR [ 2343096 ] Model Generator: Improve Reference Class Detection
 * 				<li>BF [ 2780468 ] ModelClassGenerator: not generating methods for Created*
 * 				<li>--
 * 				<li>FR [ 2848449 ] ModelClassGenerator: Implement model getters
 *					https://sourceforge.net/tracker/?func=detail&atid=879335&aid=2848449&group_id=176962
 * @author Victor Perez, e-Evolution
 * 				<li>FR [ 1785001 ] Using ModelPackage of EntityType to Generate Model Class
 * 
 * @author Andreas Sumerauer
 * 
 */
public class ModelGen
{
	final static String author = "Andreas Sumerauer";

	public static final String NL = "\n";

	/**	Logger			*/
	protected static final CLogger	log	= CLogger.getCLogger (ModelGen.class);
	protected MModelGenerator m_mmGen = null;

	/** Package Name */
	protected String packageName = "";

	private boolean m_isBaseClass;
	
	/**
	 * Generate PO Class
	 * @param tableID table id
	 * @param directory directory
	 * @param packageName package name
	 * @param columnEntityTypeFilter entity type filter for columns
	 * @param mmGen MModelGenerator object
	 * @param isBaseClass determines whether the generated source is a base class (X_) or a customizable class.
	 */
	public ModelGen (int tableID, String directory, String packageName, String columnEntityTypeFilter, MModelGenerator mmGen, boolean isBaseClass) {
		// get columns
		boolean hasColumns = getColumns(tableID, columnEntityTypeFilter);
		if (mmGen.isHasCustomColumns() && !hasColumns)
			return;
		
		// add Base Class to Imports
		m_mmGen = mmGen;
		m_isBaseClass = isBaseClass;
		this.packageName = packageName;
		
		//	create column access methods
		StringBuilder mandatory = new StringBuilder();
		StringBuilder sb = isBaseClass 
				? createColumns(tableID, mandatory) 
				: new StringBuilder("");

		// Header
		String className = createHeader(tableID, sb, mandatory, packageName);

		// Save
		if ( ! directory.endsWith(File.separator) )
			directory += File.separator;

		writeToFile (sb, directory + className + ".java");
	}

	/**************************************************************************
	 * 	Write to file
	 * 	@param sb string buffer
	 * 	@param fileName file name
	 */
	private void writeToFile (StringBuilder sb, String fileName)
	{
		try
		{
			Reader fr = null;
			File out = new File (fileName);
			if (!m_isBaseClass) {
				boolean fileExists = true;
				try {
					fr = new InputStreamReader(new FileInputStream(out), "UTF-8");
					fr.read();
				} catch (FileNotFoundException e) {
					fileExists = false;
				} finally {
					if (fileExists) {
						String msg = fileName + " already exists! To generate a fresh version of the file please delete it from the file system first.";
						log.severe(msg); 
						showMsgBox(msg);
						fr.close();
						return;
					}
				}
			} // file exists?
			Writer fw = new OutputStreamWriter(new FileOutputStream(out, false), "UTF-8");
			for (int i = 0; i < sb.length(); i++)
			{
				char c = sb.charAt(i);
				//	after
				if (c == ';' || c == '}')
				{
					fw.write (c);
					if (sb.substring(i+1).startsWith("//")) {
						//fw.write('\t');
					} else {
						//fw.write(NL);
					}
				}
				//	before & after
				else if (c == '{')
				{
					//fw.write(NL);
					fw.write (c);
					//fw.write(NL);
				}
				else
					fw.write (c);
			}
			fw.flush ();
			fw.close ();
			float size = out.length();
			size /= 1024;
			StringBuilder msgout = new StringBuilder().append(out.getAbsolutePath()).append(" - ").append(size).append(" kB");
			System.out.println(msgout.toString());
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, fileName, ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 	String representation
	 * 	@return string representation
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder("GenerateModel[").append("]");
		return sb.toString();
	}
	
	
	/** Import classes */
	protected Collection<String> s_importClasses = new TreeSet<String>();

	private ColumnData[] m_columnData;

//	private String m_baseClassPackage;
	/**
	 * Add class name to class import list
	 * @param className
	 */
	protected void addImportClass(String className) {
		if (className == null
				|| (className.startsWith("java.lang.") && !className.startsWith("java.lang.reflect."))
				|| className.startsWith(packageName+"."))
			return;
		for(String name : s_importClasses) {
			if (className.equals(name))
				return;
		}
		s_importClasses.add(className);
	}
	/**
	 * Add class to class import list
	 * @param cl
	 */
	protected void addImportClass(Class<?> cl) {
		if (cl.isArray()) {
			cl = cl.getComponentType();
		}
		if (cl.isPrimitive())
			return;
		addImportClass(cl.getCanonicalName());
	}
	/**
	 * Generate java imports
	 * @param sb
	 */
	protected void createImports(StringBuilder sb) {
		for (String name : s_importClasses) {
			sb.append("import ").append(name).append(";").append(NL);
		}
		sb.append(NL);
	}

	/**
	 * 	Add Header info to buffer
	 * 	@param AD_Table_ID table
	 * 	@param sb buffer
	 * 	@param mandatory init call for mandatory columns
	 * 	@param packageName package name
	 * 	@return class name
	 */
	protected String createHeader (int AD_Table_ID, StringBuilder sb, StringBuilder mandatory, String packageName)
	{
		String prefixBase;
		String prefixDerived;
		String baseClassPackage;
		if (m_mmGen.isCoreTable()) {
			if (m_mmGen.getCustomPrefix() == null || Util.isEmpty(m_mmGen.getCustomPrefix())) 
				throw new IllegalArgumentException("Custom Prefix required for Core Table");
			if (m_mmGen.getBaseClassPackage() == null || Util.isEmpty(m_mmGen.getBaseClassPackage())) 
				throw new IllegalArgumentException("Base Class Name required for Core Table");

			
			prefixBase = new StringBuilder("X").append(m_mmGen.getCustomPrefix()).append("_").toString();
			prefixDerived = m_mmGen.getCustomPrefix();
			baseClassPackage  = m_mmGen.getBaseClassPackage();
		} else {
			prefixBase = "X_";
			prefixDerived = "M";
			baseClassPackage = null;
		}
		
		String tableName = "";
		int accessLevel = 0;
		String sql = "SELECT TableName, AccessLevel FROM AD_Table WHERE AD_Table_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				tableName = rs.getString(1);
				accessLevel = rs.getInt(2);
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (tableName == null)
			throw new RuntimeException ("TableName not found for ID=" + AD_Table_ID);
		//

		//
		StringBuilder keyColumn = new StringBuilder().append(tableName).append("_ID");
		StringBuilder className;
		StringBuilder baseClassName;
		
		if (m_isBaseClass) {
			className = new StringBuilder(prefixBase).append(tableName);
			if (m_mmGen.isCoreTable()) {
				baseClassName =  new StringBuilder("M").append(getTrunk(tableName));
				addImportClass(baseClassPackage + "." + baseClassName);
			} else {
				baseClassName =  new StringBuilder("PO");
				this.addImportClass("org.compiere.model.PO");
			}
		} else {
			className = new StringBuilder(prefixDerived).append(getTrunk(tableName));
			baseClassName = new StringBuilder(m_mmGen.isCoreTable() ? prefixBase : "X_").append(tableName);
		}
			
		//
		StringBuilder start = new StringBuilder()
			.append (ModelInterfaceGen.COPY)
			.append (m_isBaseClass ? "/** Generated Model - DO NOT CHANGE */" : "").append(NL)
			.append("package ").append(packageName).append(";").append(NL)
			.append(NL)
		;

		addImportClass(java.util.Properties.class);
		addImportClass(java.sql.ResultSet.class);
	//	if (!packageName.equals("org.compiere.model"))
	//		addImportClass("org.compiere.model.*");

		
		StringBuilder startBottom = new StringBuilder();
		
		//	Class
		startBottom.append("/**").append(NL)
			.append(" * ").append(m_isBaseClass ? "Generated" : "Custom").append(" Model for table ").append(tableName).append(NL)
			.append(" * @author ").append(m_isBaseClass ? "iDempiere (generated)" : author).append(NL)
			.append(" * @version ").append(Adempiere.MAIN_VERSION).append(" - $Id$").append(NL)
			.append(" */").append(NL)
			.append("public class ").append(className)
		 	.append(" extends ")
		 	.append(baseClassName);
		if (m_isBaseClass && !m_mmGen.isCoreTable()) { 	
			startBottom.append(" implements I_").append(tableName)
			 	.append(", I_Persistent ");
			addImportClass("org.compiere.model.I_Persistent");
		}
		startBottom.append("{").append(NL)

			 // serialVersionUID
			 .append(NL)
			 .append("\tprivate static final long serialVersionUID = ")
			 .append(String.format("%1$tY%1$tm%1$td", new Timestamp(System.currentTimeMillis())))
		 	 .append("L;").append(NL)
			 //.append("\tprivate static final long serialVersionUID = 1L;").append(NL)

			//	Standard Constructor
			 .append(NL)
			 .append("\t/**").append(NL)
			 .append("\t * Standard Constructor").append(NL)
			 .append("\t */").append(NL)
			 .append("\tpublic ").append(className).append(" (Properties ctx, int ").append(keyColumn).append(", String trxName) {").append(NL)
			 .append("\t\tsuper (ctx, ").append(keyColumn).append(", trxName);").append(NL);
		if (mandatory.length() > 0) {	 
			startBottom.append("\t\t/** if (").append(keyColumn).append(" == 0)").append(NL)
				.append("\t\t{").append(NL)
				.append(mandatory) //.append(NL)
				.append("\t\t} */").append(NL);
		}
		startBottom.append("    }").append(NL)
			//	Constructor End

			//	Load Constructor
			 .append(NL)
			 .append("\t/**").append(NL)
			 .append("\t * Load Constructor").append(NL)
			 .append("\t */").append(NL)
			 .append("\tpublic ").append(className).append(" (Properties ctx, ResultSet rs, String trxName) {").append(NL)
			 .append("\t\tsuper (ctx, rs, trxName);").append(NL)
			 .append("\t}").append(NL);
		
			//	Load Constructor End
		// Downcast Constructor
		if (m_mmGen.isCoreTable()) {
			startBottom.append(NL)
				.append("\t/**").append(NL)
				.append("\t * Downcast Constructor").append(NL)
				.append("\t */").append(NL)
				.append("\tpublic ").append(className).append(" (X_").append(tableName).append(" base) {").append(NL)
				.append("\t\tsuper (base.getCtx(), 0, base.get_TrxName());").append(NL)
				.append("\t\tdowncast(base, this);").append(NL)
				.append("\t}").append(NL);
			addImportClass(baseClassPackage + ".X_" + tableName);
		} // Downcast Constructor End
		
		// TODO Add all constructors present in M-class if core table

			// TableName
//			 .append(NL)
//			 .append("    /** TableName=").append(tableName).append(" */").append(NL)
//			 .append("    public static final String Table_Name = \"").append(tableName).append("\";").append(NL)

			// AD_Table_ID
//			 .append(NL)
//			 .append("    /** AD_Table_ID=").append(AD_Table_ID).append(" */").append(NL)
//			 .append("    public static final int Table_ID = MTable.getTable_ID(Table_Name);").append(NL)

			// KeyNamePair
//			 .append(NL)
//			 .append("    protected static KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);").append(NL)

		if(m_isBaseClass && !m_mmGen.isCoreTable()) {
			// accessLevel
			StringBuilder accessLevelInfo = new StringBuilder().append(accessLevel).append(" ");
			if (accessLevel >= 4 )
				accessLevelInfo.append("- System ");
			if (accessLevel == 2 || accessLevel == 3 || accessLevel == 6 || accessLevel == 7)
				accessLevelInfo.append("- Client ");
			if (accessLevel == 1 || accessLevel == 3 || accessLevel == 5 || accessLevel == 7)
				accessLevelInfo.append("- Org ");

//			 .append(NL)
//			 .append("    protected BigDecimal accessLevel = BigDecimal.valueOf(").append(accessLevel).append(");").append(NL)
			startBottom.append(NL)
				.append("\t/**").append(NL)
				.append("\t * AccessLevel").append(NL)
				.append("\t * @return ").append(accessLevelInfo).append(NL)
				.append("\t */").append(NL)
				.append("\tprotected int get_AccessLevel() {").append(NL)
				.append("\t\treturn accessLevel.intValue();").append(NL)
				.append("\t}").append(NL);
			// initPO
			startBottom.append(NL)
				 .append("\t/**").append(NL)
				 .append("\t * Load Meta Data").append(NL)
				 .append("\t */").append(NL)
				 .append("\tprotected POInfo initPO (Properties ctx) {").append(NL)
				 .append("\t\tPOInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());").append(NL)
				 .append("\t\treturn poi;").append(NL)
				 .append("\t}").append(NL);
			addImportClass("org.compiere.model.POInfo");
				// initPO
	
			final String sqlCol = "SELECT COUNT(*) FROM AD_Column WHERE AD_Table_ID=? AND ColumnName=? AND IsActive='Y'";
			boolean hasName = (DB.getSQLValue(null, sqlCol, AD_Table_ID, "Name") == 1);
				// toString()
			startBottom.append(NL)
				 .append("\tpublic String toString() {").append(NL)
				 .append("\t\tStringBuilder sb = new StringBuilder (\"").append(className).append("[\")").append(NL)
				 .append("\t\t\t.append(get_ID())");
			if (hasName)
				startBottom.append(".append(\",Name=\").append(getName())");
			startBottom.append(".append(\"]\");").append(NL)
				 .append("\t\treturn sb.toString();").append(NL)
				 .append("\t}").append(NL);
		}

		createImports(start);
		start.append(startBottom);

		String end = "}";
		//
		sb.insert(0, start);
		sb.append(end);

		return className.toString();
	}


	public static String getTrunk(String tableName) {
		return getTrunk (tableName, false); 
	}	
	
	public static String getTrunk(String columnName, boolean isParameterName) {
		String trunk = columnName.startsWith("Test") || columnName.startsWith("Fact") 
			? columnName
			: columnName.substring(columnName.indexOf("_")+1);
		trunk = Util.replace(trunk, "_", "");
		if (isParameterName) {
			trunk = trunk.substring(0,1).toLowerCase() + trunk.substring(1);
		}
		return trunk;
	}
	
	protected boolean getColumns (int AD_Table_ID, String entityTypeFilter) {
		ArrayList<ColumnData> list = new ArrayList<ColumnData>();
		String sql = "SELECT c.ColumnName, c.IsUpdateable, c.IsMandatory,"		//	1..3
			+ " c.AD_Reference_ID, c.AD_Reference_Value_ID, DefaultValue, SeqNo, "	//	4..7
			+ " c.FieldLength, c.ValueMin, c.ValueMax, c.VFormat, c.Callout, "	//	8..12
			+ " c.Name, c.Description, c.ColumnSQL, c.IsEncrypted, c.IsKey, c.IsIdentifier "  // 13..18
			+ "FROM AD_Column c "
			+ "WHERE c.AD_Table_ID=?"
			+ " AND c.ColumnName NOT IN ('AD_Client_ID', 'AD_Org_ID', 'IsActive', 'Created', 'CreatedBy', 'Updated', 'UpdatedBy')"
			+ " AND c.IsActive='Y'"
			+ (!Util.isEmpty(entityTypeFilter) ? " AND c." + entityTypeFilter : "")
			+ " ORDER BY c.ColumnName";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, AD_Table_ID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ColumnData cd = new ColumnData();
				
				cd.columnName = rs.getString(1);      
				cd.isUpdateable = "Y".equals(rs.getString(2));    
				cd.isMandatory = "Y".equals(rs.getString(3));    
				cd.displayType = rs.getInt(4);      
				cd.AD_Reference_ID = rs.getInt(5);      
				cd.defaultValue = rs.getString(6);      
				cd.seqNo = rs.getInt(7);      
				cd.fieldLength = rs.getInt(8);      
				cd.ValueMin = rs.getString(9);      
				cd.ValueMax = rs.getString(10);      
				cd.VFormat = rs.getString(11);      
				cd.Callout = rs.getString(12);      
				cd.Name = rs.getString(13);      
				cd.Description = rs.getString(14);      
				cd.ColumnSQL = rs.getString(15);      
				cd.virtualColumn = cd.ColumnSQL != null && cd.ColumnSQL.length() > 0;
				cd.IsEncrypted = "Y".equals(rs.getString(16));    
				cd.IsKey = "Y".equals(rs.getString(17));    
				cd.IsIdentifier = "Y".equals(rs.getString(18));
				
				list.add(cd);
			}
		}
		catch (SQLException e) {
			throw new DBException(e, sql);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		m_columnData = list.toArray(new ColumnData[list.size()]);
		return !list.isEmpty();
	}	//	createColumns

	/**
	 * 	Create Column access methods
	 * 	@param AD_Table_ID table
	 * 	@param mandatory init call for mandatory columns
	 * 	@return set/get method
	 */
	protected StringBuilder createColumns (int AD_Table_ID, StringBuilder mandatory)
	{
		StringBuilder sb = new StringBuilder();
		boolean isKeyNamePairCreated = false; // true if the method "getKeyNamePair" is already generated

		for (ColumnData cd : m_columnData)	{
			if(m_mmGen.isCoreTable() && m_isBaseClass) {
				// Create COLUMNNAME_ property 
				sb.append(NL)
				  		.append("    /** Column name ").append(cd.columnName).append(" */")
				  		.append(NL)
				  		.append("    public static final String COLUMNNAME_").append(cd.columnName)
				  		.append(" = \"").append(cd.columnName).append("\";")
				  		.append(NL);
				//
			}
			sb.append(
				createColumnMethods (mandatory, cd, AD_Table_ID)
			);
			//
			if (cd.seqNo == 1 && cd.IsIdentifier) {
				if (!isKeyNamePairCreated) {
					sb.append(createKeyNamePair(cd.columnName, cd.displayType));
					isKeyNamePairCreated = true;
				}
				else {
					
					StringBuilder msgException = new StringBuilder("More than one primary identifier found ")
								.append(" (AD_Table_ID=").append(AD_Table_ID).append(", ColumnName=").append(cd.columnName).append(")");						
					throw new RuntimeException(msgException.toString());
				}
			}
			}
		return sb;
	}	//	createColumns

	/**
	 *	Create set/get methods for column
	 * 	@param mandatory init call for mandatory columns
	 * 	@param columnName column name
	 * 	@param isUpdateable updateable
	 * 	@param isMandatory mandatory
	 * 	@param displayType display type
	 * 	@param AD_Reference_ID validation reference
	 * 	@param fieldLength int
	 *	@param defaultValue default value
	 * 	@param ValueMin String
	 *	@param ValueMax String
	 *	@param VFormat String
	 *	@param Callout String
	 *	@param Name String
	 *	@param Description String
	 * 	@param virtualColumn virtual column
	 * 	@param IsEncrypted stored encrypted
	@return set/get method
	 */
	private String createColumnMethods (StringBuilder mandatory, ColumnData cd, int AD_Table_ID)
	{
		Class<?> clazz = ModelInterfaceGen.getClass(cd.columnName, cd.displayType, cd.AD_Reference_ID);
		String dataType = ModelInterfaceGen.getDataTypeName(clazz, cd.displayType);
		if (cd.defaultValue == null)
			cd.defaultValue = "";
		if (DisplayType.isLOB(cd.displayType))		//	No length check for LOBs
			cd.fieldLength = 0;

		//	Set	********
		String setValue = "\t\tset_Value";
		if (cd.IsEncrypted)
			setValue = "\t\tset_ValueE";
		// Handle isUpdateable
		if (!cd.isUpdateable)
		{
			setValue = "\t\tset_ValueNoCheck";
			if (cd.IsEncrypted)
				setValue = "\t\tset_ValueNoCheckE";
		}

		StringBuilder sb = new StringBuilder();

		// TODO - New functionality
		// 1) Must understand which class to reference
		if (DisplayType.isID(cd.displayType) && !cd.IsKey)
		{
			String fieldName = ModelInterfaceGen.getFieldName(cd.columnName);
			String referenceClassName = ModelInterfaceGen.getReferenceClassName(AD_Table_ID, cd.columnName, cd.displayType, cd.AD_Reference_ID, false);
			//
			if (fieldName != null && referenceClassName != null)
			{
				String typeName = referenceClassName.substring(referenceClassName.lastIndexOf(".") + 1);
				sb.append(NL)
				.append("\tpublic ").append(typeName).append(" get").append(fieldName).append("() throws RuntimeException {").append(NL)
				.append("\t\treturn (").append(typeName).append(")MTable.get(getCtx(), ").append(typeName).append(".Table_Name)").append(NL)
				.append("\t\t\t\t.getPO(get").append(cd.columnName).append("(), get_TrxName());").append(NL)
				/**/
				.append("\t}").append(NL)
				;
				// Add imports:
				addImportClass(referenceClassName);
				addImportClass("org.compiere.model.MTable");
			}
		}

		// Create Java Comment
		generateJavaSetComment(cd.columnName, cd.Name, cd.Description, sb);

		//	public void setColumn (xxx variable)
		String parameterName = getTrunk(cd.columnName, true);
		sb.append("\tpublic void set").append(cd.columnName).append(" (").append(dataType).append(" ").append(parameterName).append(") {").append(NL)
		;
				
		//	List Validation
		if (cd.AD_Reference_ID != 0 && String.class == clazz)
		{
			String staticVar = addListValidation (sb, cd.AD_Reference_ID, cd.columnName, parameterName);
			sb.insert(0, staticVar);
		}
		
		//	Payment Validation
		if (cd.displayType == DisplayType.Payment)
		{
			String staticVar = addListValidation (sb, REFERENCE_PAYMENTRULE, cd.columnName, parameterName);
			sb.insert(0, staticVar);			
		}

		//	setValue ("ColumnName", xx);
		if (cd.virtualColumn)
		{
			sb.append ("\t\tthrow new IllegalArgumentException (\"").append(cd.columnName).append(" is virtual column\");");
		}
		//	Integer
		else if (clazz.equals(Integer.class))
		{
			if (cd.columnName.endsWith("_ID"))
			{
				int firstOK = 1;
				//	check special column
				if (cd.columnName.equals("AD_Client_ID") || cd.columnName.equals("AD_Org_ID")
					|| cd.columnName.equals("Record_ID") || cd.columnName.equals("C_DocType_ID")
					|| cd.columnName.equals("Node_ID") || cd.columnName.equals("AD_Role_ID")
					|| cd.columnName.equals("M_AttributeSet_ID") || cd.columnName.equals("M_AttributeSetInstance_ID"))
					firstOK = 0;
				//	set _ID to null if < 0 for special column or < 1 for others
				sb.append("\t\tif (").append (parameterName).append (" < ").append(firstOK).append(") ").append(NL)
					.append("\t").append(setValue).append(" (").append ("COLUMNNAME_").append(cd.columnName).append(", null);").append(NL)
					.append("\t\telse ").append(NL).append("\t");
			}
			sb.append(setValue).append(" (").append ("COLUMNNAME_").append(cd.columnName).append(", Integer.valueOf(").append(parameterName).append("));").append(NL);
		}
		//		Boolean
		else if (clazz.equals(Boolean.class))
			sb.append(setValue).append(" (").append ("COLUMNNAME_").append(cd.columnName).append(", Boolean.valueOf(").append(parameterName).append("));").append(NL);
		else
		{
			sb.append(setValue).append(" (").append ("COLUMNNAME_").append (cd.columnName).append (", ")
				.append(parameterName).append (");").append(NL);
		}
		sb.append("\t}").append(NL);

		//	Mandatory call in constructor
		if (cd.isMandatory)
		{
			mandatory.append("\t\t\tset").append(cd.columnName).append(" (");
			if (clazz.equals(Integer.class))
				mandatory.append("0");
			else if (clazz.equals(Boolean.class))
			{
				if (cd.defaultValue.indexOf('Y') != -1)
					mandatory.append(true);
				else
					mandatory.append("false");
			}
			else if (clazz.equals(BigDecimal.class))
				mandatory.append("Env.ZERO");
			else if (clazz.equals(Timestamp.class))
				mandatory.append("new Timestamp( System.currentTimeMillis() )");
			else
				mandatory.append("null");
			mandatory.append(");").append(NL);
			if (cd.defaultValue.length() > 0)
				mandatory.append("// ").append(cd.defaultValue).append(NL);
		}


		//	****** Get Comment ******
		generateJavaGetComment(cd.Name, cd.Description, sb);

		//	Get	********
		String getValue = "get_Value";
		if (cd.IsEncrypted)
			getValue = "get_ValueE";

		sb.append("\tpublic ").append(dataType);
		if (clazz.equals(Boolean.class))
		{
			sb.append(" is");
			if (cd.columnName.toLowerCase().startsWith("is"))
				sb.append(cd.columnName.substring(2));
			else
				sb.append(cd.columnName);
		} else {
			sb.append(" get").append(cd.columnName);
		}
		sb.append(" () {").append(NL)
			.append("\t\t");
		if (clazz.equals(Integer.class)) {
			sb.append("Integer ii = (Integer)").append(getValue).append("(").append ("COLUMNNAME_").append(cd.columnName).append(");").append(NL)
				.append("\t\tif (ii == null)").append(NL)
				.append("\t\t\t return 0;").append(NL)
				.append("\t\treturn ii.intValue();").append(NL);
		}
		else if (clazz.equals(BigDecimal.class)) {
			sb.append("BigDecimal bd = (BigDecimal)").append(getValue).append("(").append ("COLUMNNAME_").append(cd.columnName).append(");").append(NL)
				.append("\t\tif (bd == null)").append(NL)
				.append("\t\t\t return Env.ZERO;").append(NL)
				.append("\t\treturn bd;").append(NL);
			addImportClass(java.math.BigDecimal.class);
			addImportClass(org.compiere.util.Env.class);
		}
		else if (clazz.equals(Boolean.class)) {
			sb.append("Object oo = ").append(getValue).append("(").append ("COLUMNNAME_").append(cd.columnName).append(");").append(NL)
				.append("\t\tif (oo != null) ").append(NL)
				.append("\t\t{").append(NL)
				.append("\t\t\t if (oo instanceof Boolean) ").append(NL)
				.append("\t\t\t\t return ((Boolean)oo).booleanValue(); ").append(NL)
				.append("\t\t\treturn \"Y\".equals(oo);").append(NL)
				.append("\t\t}").append(NL)
				.append("\t\treturn false;").append(NL);
		}
		else if (dataType.equals("Object")) {
			sb.append("\t\treturn ").append(getValue)
				.append("(").append ("COLUMNNAME_").append(cd.columnName).append(");").append(NL);
		}
		else {
			sb.append("return (").append(dataType).append(")").append(getValue)
				.append("(").append ("COLUMNNAME_").append(cd.columnName).append(");").append(NL);
			addImportClass(clazz);
		}
		sb.append("\t}").append(NL);
		//
		return sb.toString();
	}	//	createColumnMethods


	//	****** Set Comment ******
	public void generateJavaSetComment(String columnName, String propertyName, String description, StringBuilder result) {

		result.append(NL)
			.append("\t/**").append(NL)
			.append("\t * Set ").append(propertyName).append(".").append(NL)
			.append("\t * @param ").append(columnName).append(" ")
		;
		if (description != null && description.length() > 0) {
			result.append(NL)
				.append("\t * ").append(description).append(NL);
		} else {
			result.append(propertyName).append(NL);
		}
		result.append("\t */").append(NL);
	}

	//	****** Get Comment ******
	public void generateJavaGetComment(String propertyName, String description, StringBuilder result) {

		result.append(NL)
			.append("\t/**").append(NL)
			.append("\t * Get ").append(propertyName);
		if (description != null && description.length() > 0) {
			result.append(".").append(NL)
				.append("\t * @return ").append(description).append(NL);
		} else {
			result.append(".\n\t * @return ").append(propertyName).append(NL);
		}
		result.append("\t */").append(NL);
	}

	/**
	 * 	Add List Validation
	 * 	@param sb buffer - example:
		if (NextAction.equals("N") || NextAction.equals("F"));
		else throw new IllegalArgumentException ("NextAction Invalid value - Reference_ID=219 - N - F");
	 * @param referenceID reference
	 * @param columnName column
	 * @param parameterName TODO
	 * 	@return static parameter - Example:
		public static final int NEXTACTION_AD_Reference_ID=219;
		public static final String NEXTACTION_None = "N";
		public static final String NEXTACTION_FollowUp = "F";
	 */
	private String addListValidation (StringBuilder sb, int referenceID,
		String columnName, String parameterName)
	{
		StringBuilder retValue = new StringBuilder();
		if (referenceID <= MTable.MAX_OFFICIAL_ID)
		{
			retValue.append("\n\t/** ").append(columnName).append(" AD_Reference_ID=").append(referenceID) .append(" */")
				.append("\n\tpublic static final int ").append(columnName.toUpperCase())
				.append("_AD_Reference_ID=").append(referenceID).append(";");
		}
		//
		boolean found = false;
		StringBuilder values = new StringBuilder("Reference_ID=")
			.append(referenceID);
		StringBuilder statement = new StringBuilder();
		//
		String sql = "SELECT Value, Name FROM AD_Ref_List WHERE AD_Reference_ID=? ORDER BY Value";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, referenceID);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String value = rs.getString(1);
				values.append(" - ").append(value);
				if (statement.length() == 0)
					statement.append("\n\t\tif (").append(columnName)
						.append(".equals(\"").append(value).append("\")");
				else
					statement.append(" || ").append(columnName)
						.append(".equals(\"").append(value).append("\")");
				//
				if (!found)
				{
					found = true;
				}


				//	Name (SmallTalkNotation)
				String name = rs.getString(2);
				char[] nameArray = name.toCharArray();
				StringBuilder nameClean = new StringBuilder();
				boolean initCap = true;
				for (int i = 0; i < nameArray.length; i++)
				{
					char c = nameArray[i];
					if (Character.isJavaIdentifierPart(c))
					{
						if (initCap)
							nameClean.append(Character.toUpperCase(c));
						else
							nameClean.append(c);
						initCap = false;
					}
					else
					{
						if (c == '+')
							nameClean.append("Plus");
						else if (c == '-')
							nameClean.append("_");
						else if (c == '>')
						{
							if (name.indexOf('<') == -1)	//	ignore <xx>
								nameClean.append("Gt");
						}
						else if (c == '<')
						{
							if (name.indexOf('>') == -1)	//	ignore <xx>
								nameClean.append("Le");
						}
						else if (c == '!')
							nameClean.append("Not");
						else if (c == '=')
							nameClean.append("Eq");
						else if (c == '~')
							nameClean.append("Like");
						initCap = true;
					}
				}
				retValue.append("\n\t/** ").append(name).append(" = ").append(value).append(" */");
				retValue.append("\n\tpublic static final String ").append(columnName.toUpperCase())
					.append("_").append(nameClean)
					.append(" = \"").append(value).append("\";");
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		statement.append(")")
			.append("; ")
			.append("else ")
			.append("throw new IllegalArgumentException (\"").append(columnName)
			.append(" Invalid value - \" + ").append(columnName)
			.append(" + \" - ").append(values).append("\");");
		// [1762461] - Remove hardcoded list items checking in generated models
		// if (found && !columnName.equals("EntityType"))
		//	sb.append (statement);
		sb.append("\n");
		return retValue.toString();
	}	//	addListValidation

	/**
	 * 	Create getKeyNamePair() method with first identifier
	 *	@param columnName name
	 *	 * @param displayType int
	@return method code
	 */
	private StringBuilder createKeyNamePair (String columnName, int displayType)
	{
		StringBuilder method = new StringBuilder("get").append(columnName).append("()");
		if (displayType != DisplayType.String)
			method = new StringBuilder("String.valueOf(").append(method).append(")");

		StringBuilder sb = new StringBuilder(NL)
			.append("\t/**").append(NL)
			.append("\t * Get Record ID/ColumnName").append(NL)
			.append("\t * @return ID/ColumnName pair").append(NL)
			.append("\t */").append(NL)
			.append("\tpublic KeyNamePair getKeyNamePair() {").append(NL)
			.append("\t\treturn new KeyNamePair(get_ID(), ").append(method).append(");").append(NL)
			.append("\t}").append(NL)
		;
		addImportClass(org.compiere.util.KeyNamePair.class);
		return sb;
	}	//	createKeyNamePair

	
	
	/**
	 * 
	 * @param mmGen
	 * @param isBaseClass
	 */
	public static void generateSource(MModelGenerator mmGen, boolean isBaseClass)
	{
		if (mmGen.getFolder() == null || mmGen.getFolder().trim().length() == 0)
			throw new IllegalArgumentException("Must specify source folder");

		File file = new File(mmGen.getFolder());
		if (!file.exists())
			throw new IllegalArgumentException("Source folder doesn't exists. sourceFolder="+mmGen.getFolder());

		if (mmGen.getPackageName() == null || mmGen.getPackageName().trim().length() == 0)
			throw new IllegalArgumentException("Must specify package name");

		if (mmGen.getTableName() == null || mmGen.getTableName().trim().length() == 0)
			throw new IllegalArgumentException("Must specify table name");
		
		StringBuilder tableLike = new StringBuilder().append(mmGen.getTableName().trim());
		if (!tableLike.toString().startsWith("'") || !tableLike.toString().endsWith("'"))
			tableLike = new StringBuilder("'").append(tableLike).append("'");

		StringBuilder tableEntityTypeFilter = new StringBuilder();
		if (mmGen.getTableEntityTypeFilter() != null && mmGen.getTableEntityTypeFilter().trim().length() > 0) {
			tableEntityTypeFilter.append("EntityType IN (");
			StringTokenizer tokenizer = new StringTokenizer(mmGen.getTableEntityTypeFilter(), ",");
			int i = 0;
			while(tokenizer.hasMoreTokens()) {
				StringBuilder token = new StringBuilder().append(tokenizer.nextToken().trim());
				if (!token.toString().startsWith("'") || !token.toString().endsWith("'"))
					token = new StringBuilder("'").append(token).append("'");
				if (i > 0)
					tableEntityTypeFilter.append(",");
				tableEntityTypeFilter.append(token);
				i++;
			}
			tableEntityTypeFilter.append(")");
		} else {
			tableEntityTypeFilter.append("EntityType IN ('U','A')");
		}
		StringBuilder directory = new StringBuilder().append(mmGen.getFolder().trim());
		String packagePath = mmGen.getPackageName().replace(".", File.separator);
		if (!(directory.toString().endsWith("/") || directory.toString().endsWith("\\"))) {
			directory.append(File.separator);
		}
		if (File.separator.equals("/"))
			directory = new StringBuilder(directory.toString().replaceAll("[\\\\]", File.separator));
		else
			directory = new StringBuilder(directory.toString().replaceAll("[/]", File.separator));
		directory.append(packagePath);
		file = new File(directory.toString());
		if (!file.exists())
			file.mkdirs();

		//	complete sql
		String filterViews = null;
		if (tableLike.toString().contains("%")) {
			filterViews = "AND (TableName IN ('RV_WarehousePrice','RV_BPartner') OR IsView='N')"; 	//	special views
		}
		if (tableLike.toString().equals("'%'")) {
			filterViews += " AND TableName NOT LIKE 'W|_%' ESCAPE '|'"; 	//	exclude webstore from general model generator
		}
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT AD_Table_ID ")
			.append("FROM AD_Table ")
			.append("WHERE IsActive = 'Y' AND TableName NOT LIKE '%_Trl' ");
		// Autodetect if we need to use IN or LIKE clause - teo_sarca [ 3020640 ]
		if (tableLike.indexOf(",") == -1)
			sql.append(" AND TableName LIKE ").append(tableLike);
		else
			sql.append(" AND TableName IN (").append(tableLike).append(")"); // only specific tables
		sql.append(" AND ").append(tableEntityTypeFilter.toString());
		if (filterViews != null) {
			sql.append(filterViews);
		}
		sql.append(" ORDER BY TableName");
		//
		StringBuilder columnFilterBuilder = new StringBuilder();
		if (!Util.isEmpty(mmGen.getColumnEntityTypeFilter(), true)) {
			columnFilterBuilder.append("EntityType IN (");
			StringTokenizer tokenizer = new StringTokenizer(mmGen.getColumnEntityTypeFilter(), ",");
			int i = 0;
			while(tokenizer.hasMoreTokens()) {
				StringBuilder token = new StringBuilder().append(tokenizer.nextToken().trim());
				if (!token.toString().startsWith("'") || !token.toString().endsWith("'"))
					token = new StringBuilder("'").append(token).append("'");
//				if (token.toString().equals("'D'"))
//					throw new IllegalArgumentException("Core columns can not be processed.");
				if (i > 0)
					columnFilterBuilder.append(",");
				columnFilterBuilder.append(token);
				i++;
			}
			columnFilterBuilder.append(")");
		}
		String columnFilter = columnFilterBuilder.length() > 0 ? columnFilterBuilder.toString() : null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql.toString(), null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				new ModelGen(rs.getInt(1), directory.toString(), mmGen.getPackageName(), columnFilter, mmGen, isBaseClass);
			}
		}
		catch (SQLException e) {
			throw new DBException(e, sql.toString());
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
	}
	
	void showMsgBox(String msg) {
		AEnv.executeAsyncDesktopTask(new Runnable() {
			@Override
			public void run() {
				Messagebox.showDialog(
						msg, 
						"Model Generator", 
						Messagebox.OK, 
						Messagebox.INFORMATION);
			}
		});		
	}	
	
	private String getSerialVersionUID(String fileName) {
		File file = new File(fileName);

		try (Scanner scanner = new Scanner(file)){
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        if(line.startsWith("\tprivate static final long serialVersionUID = ")) { 
		            return line;
		        }
		    }
		} catch(FileNotFoundException e) { 
		   throw new AdempiereException(e); 
		}
		return null;
	}
	
	class ColumnData {
		public String columnName;
		public boolean isUpdateable;
		public boolean isMandatory;
		public int displayType;
		public int AD_Reference_ID;
		public String defaultValue;
		public int seqNo;
		public int fieldLength;
		public String ValueMin;
		public String ValueMax;
		public String VFormat;
		public String Callout;
		public String Name;
		public String Description;
		public String ColumnSQL;
		public boolean virtualColumn;
		public boolean IsEncrypted;
		public boolean IsKey;
		public boolean IsIdentifier;
	}
	
}
