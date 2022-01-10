/******************************************************************************
 * Copyright (C) 2021 Andreas Sumerauer                                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/

package com.ingeint.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Messagebox;
import org.compiere.Adempiere;
import org.compiere.util.DB;
import org.compiere.util.Util;

import com.ingeint.process.ModelGenProcessBase;

/**
 *  Generate MModel Classes extending the corresponding X_Model Class.
 *
 */
public class CustomizableModelGen extends ModelGen
{
	/**
	 * 	Generate PO Class
	 * 	@param AD_Table_ID table id
	 * @param directory directory
	 * @param packageName package name
	 * @param process TODO
	 */
	public CustomizableModelGen (int AD_Table_ID, String directory, String packageName, ModelGenProcessBase process)
	{
		super(AD_Table_ID, directory, packageName, (String)null, process);

		StringBuilder sb = new StringBuilder();

		// Header
		String className = createSource(AD_Table_ID, sb, packageName);

		// Save
		if ( ! directory.endsWith(File.separator) )
			directory += File.separator;

		writeToFile (sb, directory + className + ".java");
/*		
		ObjectStreamClass c = ObjectStreamClass.lookup(MyClass.class);
		long serialID = c.getSerialVersionUID();
		System.out.println(serialID);
*/		
	}

	/**
	 * 	Add Header info to buffer
	 * 	@param AD_Table_ID table
	 * 	@param sb buffer
	 * 	@param packageName package name
	 * 	@return class name
	 */
	private String createSource (int AD_Table_ID, StringBuilder sb, String packageName)
	{
		String tableName = "";
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

		// Strip Prefix ((if it exists)
		String tnStripped = tableName.substring(tableName.indexOf("_")+1);
		//
		StringBuilder baseClassName = new StringBuilder("X_").append(tableName);		
		StringBuilder className = new StringBuilder("M").append(Util.replace(tnStripped, "_", ""));
		StringBuilder keyColumn = new StringBuilder().append(tableName).append("_ID");
		//

		sb.append (ModelInterfaceGen.COPY)
		//	.append ("/** Generated Model - DO NOT CHANGE */").append(NL)
			.append("package ").append(packageName).append(";").append(NL)
			.append(NL)
		;

		addImportClass(java.util.Properties.class);
		addImportClass(java.sql.ResultSet.class);
		addImportClass(packageName + "." + baseClassName);
		createImports(sb);
		//	Class
		sb.append("/** Generated Model for ").append(tableName).append(NL)
			 .append(" *  @author iDempiere (generated) ").append(NL)
			 .append(" *  @version ").append(Adempiere.MAIN_VERSION).append(" - $Id$ */").append(NL)
			 .append("public class ").append(className)
			 .append(" extends ").append(baseClassName)
			 .append(NL)
			 .append("{").append(NL)

			 // serialVersionUID
			 .append(NL)
			 .append("\t/**").append(NL)
			 .append("\t *").append(NL)
			 .append("\t */").append(NL)
			 .append("\tprivate static final long serialVersionUID = ")
			 .append(String.format("%1$tY%1$tm%1$td", new Timestamp(System.currentTimeMillis())))
		 	 .append("L;").append(NL)
			 //.append("\tprivate static final long serialVersionUID = 1L;").append(NL)

			//	Standard Constructor
			 .append(NL)
			 .append("    /** Standard Constructor */").append(NL)
			 .append("    public ").append(className).append(" (Properties ctx, int ").append(keyColumn).append(", String trxName)").append(NL)
			 .append("    {").append(NL)
			 .append("      super (ctx, ").append(keyColumn).append(", trxName);").append(NL)
			 .append("    }").append(NL)
			//	Constructor End

			//	Load Constructor
			 .append(NL)
			 .append("    /** Load Constructor */").append(NL)
			 .append("    public ").append(className).append(" (Properties ctx, ResultSet rs, String trxName)").append(NL)
			 .append("    {").append(NL)
			 .append("      super (ctx, rs, trxName);").append(NL)
			 .append("    }").append(NL)
			//	Load Constructor End

			 .append("}").append(NL);

		//
		return className.toString();
	}



	/**************************************************************************
	 * 	Write to file
	 * 	@param sb string buffer
	 * 	@param fileName file name
	 */
	private void writeToFile (StringBuilder sb, String fileName)
	{
		Reader fr = null;
		try
		{
			File out = new File (fileName);
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

	/**
	 * @param sourceFolder
	 * @param packageName
	 * @param entityType
	 * @param process TODO
	 * @param tableLike
	 * @param columnEntityType
	 */
	public static void generateSource(String sourceFolder, String packageName, String entityType, String tableName, ModelGenProcessBase process)
	{
		if (sourceFolder == null || sourceFolder.trim().length() == 0)
			throw new IllegalArgumentException("Must specify source folder");

		File file = new File(sourceFolder);
		if (!file.exists())
			throw new IllegalArgumentException("Source folder doesn't exists. sourceFolder="+sourceFolder);

		if (packageName == null || packageName.trim().length() == 0)
			throw new IllegalArgumentException("Must specify package name");

		if (tableName == null || tableName.trim().length() == 0)
			throw new IllegalArgumentException("Must specify table name");

		StringBuilder tableLike = new StringBuilder().append(tableName.trim());
		if (!tableLike.toString().startsWith("'") || !tableLike.toString().endsWith("'"))
			tableLike = new StringBuilder("'").append(tableLike).append("'");

		StringBuilder entityTypeFilter = new StringBuilder();
		if (entityType != null && entityType.trim().length() > 0)
		{
			entityTypeFilter.append("EntityType IN (");
			StringTokenizer tokenizer = new StringTokenizer(entityType, ",");
			int i = 0;
			while(tokenizer.hasMoreTokens()) {
				StringBuilder token = new StringBuilder().append(tokenizer.nextToken().trim());
				if (!token.toString().startsWith("'") || !token.toString().endsWith("'"))
					token = new StringBuilder("'").append(token).append("'");
				if (i > 0)
					entityTypeFilter.append(",");
				entityTypeFilter.append(token);
				i++;
			}
			entityTypeFilter.append(")");
		}
		else
		{
			entityTypeFilter.append("EntityType IN ('U','A')");
		}

		StringBuilder directory = new StringBuilder().append(sourceFolder.trim());
		String packagePath = packageName.replace(".", File.separator);
		if (!(directory.toString().endsWith("/") || directory.toString().endsWith("\\")))
		{
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
		sql.append(" AND ").append(entityTypeFilter.toString());
		if (filterViews != null) {
			sql.append(filterViews);
		}
		sql.append(" ORDER BY TableName");
		//

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql.toString(), null);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				new CustomizableModelGen(rs.getInt(1), directory.toString(), packageName, process);
			}
		}
		catch (SQLException e)
		{
			throw new DBException(e, sql.toString());
		}
		finally
		{
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
}

