/**
 * This file is part of iDempiere ERP <http://www.idempiere.org>.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Copyright (C) 2015 INGEINT <http://www.ingeint.com>.
 * Copyright (C) Contributors.
 * 
 * Contributors:
 *    - 2015 Saúl Piña <spina@ingeint.com>.
 */

package com.ingeint.component;

import org.compiere.process.ProcessCall;

import com.ingeint.base.CustomProcessFactory;
import com.ingeint.process.IngDatabaseTableColumnRename;
import com.ingeint.process.IngColumnSync;
import com.ingeint.process.IngDatabaseElementColumnRename;

/**
 * Process Factory
 */
public class ProcessFactory extends CustomProcessFactory {
	
	@Override
	public ProcessCall newProcessInstance(String className) {
		ProcessCall pc = super.newProcessInstance(className);
		if(pc != null)
			return pc;
		
		switch (className) {
		case "com.ingeint.process.IngColumnSync":
			return new IngColumnSync();	
		case "com.ingeint.process.IngDatabaseElementColumnRename":
			return new IngDatabaseElementColumnRename();	
		case "com.ingeint.process.IngDatabaseTableColumnRename":
			return new IngDatabaseTableColumnRename();	
		default:
			return null;
		}
	}
	
	

	/**
	 * For initialize class. Register the process to build
	 * 
	 * <pre>
	 * protected void initialize() {
	 * 	registerProcess(PPrintPluginInfo.class);
	 * }
	 * </pre>
	 */
	@Override
	protected void initialize() {
		registerProcess(com.ingeint.process.PPrintPluginInfo.class);
		registerProcess(com.ingeint.process.ModelGenProcess.class);
		registerProcess(com.ingeint.process.CustomizableModelGenProcess.class);
		registerProcess(com.ingeint.process.ModelGenMenuProcess.class);
	}

}
