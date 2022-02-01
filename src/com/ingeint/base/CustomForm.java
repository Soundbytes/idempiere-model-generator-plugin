package com.ingeint.base;

import org.adempiere.webui.panel.ADForm;


public class CustomForm extends ADForm
{
	private static final long serialVersionUID = -8498084996736578534L;
	boolean isEmbedded = true;
	
	public CustomForm() {
		this(false);
	}
	
	public CustomForm(boolean isEmbedded) {
		setWindowMode(isEmbedded);
	}

	public void setWindowMode(boolean isEmbedded) {
		this.isEmbedded = isEmbedded;
	}
	
	@Override
	public void dispose() {
	    if(isEmbedded)
	    	super.dispose();
	    else
	    	onClose();
	}
	
	@Override
	protected void initForm() {
		((CustomFormController)getICustomForm()).initForm();
	}	
	
	
	@Override
	public Mode getWindowMode() {
		return isEmbedded ? Mode.EMBEDDED : Mode.HIGHLIGHTED;
	}
	
	@Override
	public boolean isClosable() {
		return true;
	}
}
