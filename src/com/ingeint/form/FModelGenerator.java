package com.ingeint.form;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.component.Checkbox;
import org.adempiere.webui.component.Column;
import org.adempiere.webui.component.Columns;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WChosenboxListEditor;
import org.adempiere.webui.editor.WStringEditor;
import org.adempiere.webui.editor.WTableDirEditor;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MColumn;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MTable;
import org.compiere.util.CLogger;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Trx;
import org.compiere.util.TrxRunnable;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;
import org.zkoss.zul.South;

import com.ingeint.base.CustomFormController;
import com.ingeint.base.CustomForm;
import com.ingeint.model.MModelGenerator;
import com.ingeint.util.ModelGen;
import com.ingeint.util.ModelInterfaceGen;

public class FModelGenerator  extends CustomFormController{
	
	final CLogger log =  CLogger.getCLogger(getClass());
	
	MModelGenerator gen;	

	private Borderlayout contentPane;
	private ConfirmPanel confirmPanel;
	
	public Grid northPanel;
	public Grid parameterPanel;

	private North north;
	public Center center;
	
	protected Label folderLabel;
	protected WStringEditor folderEditor;
	
	protected Label tableIDLabel;
	protected WTableDirEditor tableIDEditor;
	
	protected Label colEntityTypeLabel;
	protected WChosenboxListEditor colEntityTypeEditor;
	
	protected Label tableEntityTypeLabel;
	protected WChosenboxListEditor tableEntityTypeEditor;
	
	protected Label packageLabel;
	protected WStringEditor packageEditor;

	protected Label isCoreLabel;
	protected Checkbox isCoreCB;
	
	protected Label prefixLabel;
	protected WStringEditor prefixEditor;
	
	protected Label baseClassLabel;
	protected WStringEditor baseClassEditor;

	protected Label createBaseLabel;
	protected Checkbox createBaseCB;
	
	protected Label createCustomLabel;
	protected Checkbox createCustomCB;

	@Override
	public void buildForm() throws Exception {
		contentPane = new Borderlayout();
		confirmPanel = new ConfirmPanel(true, true, false, false, false, false, false);
		
		northPanel = GridFactory.newGridLayout();
		parameterPanel = GridFactory.newGridLayout();

		north = new North();
		center = new Center();
		
		folderLabel = new Label("Source Folder");
		folderEditor = new WStringEditor();
		folderEditor.getComponent().setWidth("100%");
		
		tableIDLabel = new Label(Msg.getElement(Env.getCtx(), "AD_Table_ID", false));
		
		colEntityTypeLabel = new Label(Msg.getElement(Env.getCtx(), "ColumnEntityTypeFilter", false));
		
		tableEntityTypeLabel = new Label(Msg.getElement(Env.getCtx(), "TableEntityTypeFilter", false));
		
		packageLabel = new Label(Msg.getElement(Env.getCtx(), "PackageName", false));
		packageEditor = new WStringEditor();
		packageEditor.getComponent().setWidth("100%");
	
		isCoreLabel = new Label(Msg.getElement(Env.getCtx(), "IsCoreTable", false));
		isCoreCB = new Checkbox();
		isCoreCB.addActionListener(this);
		
		prefixLabel = new Label(Msg.getElement(Env.getCtx(), "CustomPrefix", false));
		prefixEditor = new WStringEditor();
		prefixEditor.getComponent().setWidth("100%");
		
		baseClassLabel = new Label(Msg.getElement(Env.getCtx(), "BaseClassPackage", false));
		baseClassEditor = new WStringEditor();
		baseClassEditor.getComponent().setWidth("100%");
	
		createBaseLabel = new Label("Create Model Base Class");
		createBaseCB = new Checkbox();
		
		createCustomLabel = new Label("Create Custom Class");
		createCustomCB = new Checkbox();		
		
		((CustomForm)getForm()).setWindowMode(false);
		getForm().setTitle("Model Generator");
		
		try {
			getForm().appendChild(contentPane);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			throw e;
		}

		contentPane.appendChild(north);
		north.appendChild(northPanel);
		north.setCollapsible(true);
		north.setSplittable(true);
		LayoutUtils.addSlideSclass(north);
		
		center.appendChild(parameterPanel);
		contentPane.appendChild(center);
		
		Columns columns = new Columns();
		parameterPanel.appendChild(columns);
		Column column = new Column();
		columns.appendChild(column);		
		column = new Column();
		column.setWidth("250px");
		columns.appendChild(column);
		column.setWidth("650px");
		
		
		South south = new South();
		contentPane.appendChild(south);
		south.appendChild(confirmPanel);
		
		confirmPanel.addActionListener(this);
		
		
		
		int height = 450;
		int width = 900;
		ZKUpdateUtil.setWidth(getForm(), width + "px");
		ZKUpdateUtil.setHeight(getForm(), height + "px");


//		int windowNo = getForm().getGridTab().getWindowNo();

		MLookup lu = null;

		lu = MLookupFactory.get (Env.getCtx(), 0, 0, 
				MColumn.getColumn_ID(MModelGenerator.Table_Name, MModelGenerator.COLUMNNAME_ColumnEntityTypeFilter), 
				DisplayType.ChosenMultipleSelectionTable);
		colEntityTypeEditor = new WChosenboxListEditor(lu, "", "", true, false, true);
		
		lu = MLookupFactory.get (Env.getCtx(), 0, 0, 
				MColumn.getColumn_ID(MModelGenerator.Table_Name, MModelGenerator.COLUMNNAME_TableEntityTypeFilter), 
				DisplayType.ChosenMultipleSelectionTable);
		tableEntityTypeEditor = new WChosenboxListEditor(lu, "", "", true, false, true);

		lu = MLookupFactory.get (Env.getCtx(), 0, 0, 
				MColumn.getColumn_ID(MTable.Table_Name, MTable.COLUMNNAME_AD_Table_ID), 
				DisplayType.TableDir);
		tableIDEditor = new WTableDirEditor(lu, "", "", true, true, true);
		tableIDEditor.getComponent().setWidth("100%");
		
		
		Rows rows = (Rows) parameterPanel.newRows();
		Row row = rows.newRow();
		row.appendChild(folderLabel.rightAlign());
		row.appendChild(folderEditor.getComponent());
		row = rows.newRow();
		row.appendChild(tableIDLabel.rightAlign());
		row.appendChild(tableIDEditor.getComponent());
		row = rows.newRow();
		row.appendChild(packageLabel.rightAlign());
		row.appendChild(packageEditor.getComponent());
		row = rows.newRow();
		row.appendChild(colEntityTypeLabel.rightAlign());
		row.appendChild(colEntityTypeEditor.getComponent());
		row = rows.newRow();
		row.appendChild(tableEntityTypeLabel.rightAlign());
		row.appendChild(tableEntityTypeEditor.getComponent());
		row = rows.newRow();
		row.appendChild(isCoreLabel.rightAlign());
		row.appendChild(isCoreCB);
		row = rows.newRow();
		row.appendChild(prefixLabel.rightAlign());
		row.appendChild(prefixEditor.getComponent());
		row = rows.newRow();
		row.appendChild(baseClassLabel.rightAlign());
		row.appendChild(baseClassEditor.getComponent());
		row = rows.newRow();
		row.appendChild(createBaseLabel.rightAlign());
		row.appendChild(createBaseCB);
		row = rows.newRow();
		row.appendChild(createCustomLabel.rightAlign());
		row.appendChild(createCustomCB);
	}	
	
	private void fillForm() {
		int table_ID = getForm().getGridTab().getRecord_ID();
		gen = MModelGenerator.get(table_ID, null);
		folderEditor.setValue(gen.getFolder());
		tableIDEditor.setValue(gen.getING_Table_ID());
		packageEditor.setValue(gen.getPackageName());
		colEntityTypeEditor.setValue(gen.getColumnEntityTypeFilter());
		tableEntityTypeEditor.setValue(gen.getTableEntityTypeFilter());
		isCoreCB.setChecked(gen.isCoreTable());
		prefixEditor.setValue(gen.getCustomPrefix());
		baseClassEditor.setValue(gen.getBaseClassPackage());
		createBaseCB.setChecked(true);
		createCustomCB.setChecked(false);
		
		prefixEditor.setReadWrite(isCoreCB.isChecked());
		baseClassEditor.setReadWrite(isCoreCB.isChecked());		
	}
	
	@Override
	protected void initForm() {
		fillForm();
	}
	

	public void onEvent(Event e) throws Exception
	{
		//  OK - Save
		if (e.getTarget().getId().equals(ConfirmPanel.A_OK)) {
			try {
				Trx.run(new TrxRunnable() {
					public void run(String trxName)	{
						persist(trxName);
						generate(trxName);
					}
				});
				getForm().dispose();
			}
			catch (Exception ex) {
				FDialog.error(getForm().getWindowNo(), getForm(), "Error", ex.getLocalizedMessage());
			}
		}
		//  Cancel
		else if (e.getTarget().getId().equals(ConfirmPanel.A_CANCEL)) {
			getForm().dispose();
		}
		else if (e.getTarget().equals(isCoreCB)) {
			prefixEditor.setReadWrite(isCoreCB.isChecked());
			baseClassEditor.setReadWrite(isCoreCB.isChecked());
		}
	}

	private void generate(String trxName) {
		if (createBaseCB.isChecked()) {
			if (!gen.isCoreTable())
				ModelInterfaceGen.generateSource(gen);
			ModelGen.generateSource(gen, true);	
		}
		if (createCustomCB.isChecked()) {
			ModelGen.generateSource(gen, false);	
		}		
	}



	private void persist(String trxName) {
		gen.setFolder((String) folderEditor.getValue());
		gen.setING_Table_ID((int)tableIDEditor.getValue());
		gen.setTableName(tableIDEditor.getDisplay());
		gen.setPackageName((String) packageEditor.getValue());
		gen.setColumnEntityTypeFilter((String) colEntityTypeEditor.getValue());
		gen.setTableEntityTypeFilter((String) tableEntityTypeEditor.getValue());
		gen.setIsCoreTable(isCoreCB.isChecked());
		gen.setCustomPrefix((String) prefixEditor.getValue());
		gen.setBaseClassPackage((String) baseClassEditor.getValue());
		gen.saveEx();
	}

}
