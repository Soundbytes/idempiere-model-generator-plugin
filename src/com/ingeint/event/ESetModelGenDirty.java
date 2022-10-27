package com.ingeint.event;

import org.compiere.model.MColumn;

import com.ingeint.base.CustomEventHandler;
import com.ingeint.model.MModelGenerator;


public class ESetModelGenDirty extends CustomEventHandler {

	@Override
	protected void doHandleEvent() {
		MColumn column = (MColumn) getPO();
		MModelGenerator mg = MModelGenerator.get(column);
		if (mg != null) {
			mg.setIsDirty(true);
			mg.saveEx();
		}
	}
}
