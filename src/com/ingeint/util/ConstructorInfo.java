package com.ingeint.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import org.adempiere.exceptions.AdempiereException;


public class ConstructorInfo {
	private final Constructor<?> [] c;
	private final String[] parameters;
	private final String[] arguments;
	private final ArrayList<String> imports;
	
	public ConstructorInfo(Class<?> clazz) {
		c = clazz.getConstructors();
		parameters = new String[c.length];
		arguments = new String[c.length];
		imports = new ArrayList<>();
		init();
	}
	
	public static ConstructorInfo get(String className) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new AdempiereException(e);
		}
		return new ConstructorInfo(clazz);
	}
	

	private void init() {
		for(int i=0; i<c.length; ++i) {
			Parameter[] p = c[i].getParameters();
			StringBuilder par = new StringBuilder("(");
			StringBuilder arg = new StringBuilder("(");
			int last = p.length - 1;
			for (int k=0; k<p.length; ++k) {
				String type = p[k].getType().getCanonicalName(); // .toString();
				if (type.contains(".")){
					addImport(type);
					type = type.substring(type.lastIndexOf(".") + 1);
				}
				par.append(type).append(" ").append(p[k].getName())
						.append(k != last ? ", " : ")");
				arg.append(p[k].getName())
						.append(k != last ? ", " : ")");
			}
			parameters[i] = par.toString();
			arguments[i] = arg.toString();
		}
	}
	
	private void addImport(String imprt) {
		for (String i : imports) {
			if (i.equals(imprt))
				return;
		}
		imports.add(imprt);
	}

	public String[] getParameters() {
		return parameters;
	}

	public String[] getArguments() {
		return arguments;
	}
	
	public String[] getImports() {
		return imports.toArray(new String[imports.size()]);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Parameters:").append("\n");
		for (int i = 0; i < getParameters().length; ++i)
			sb.append("\t").append(getParameters()[i]).append("\n");
		sb.append("Arguments:").append("\n");
		for (int i = 0; i < getArguments().length; ++i)
			sb.append("\t").append(getArguments()[i]).append("\n");
		sb.append("Imports:").append("\n");
		for (int i = 0; i < getImports().length; ++i)
			sb.append("\t").append(getImports()[i]).append("\n");
		
		return sb.toString();
	}
	
}
