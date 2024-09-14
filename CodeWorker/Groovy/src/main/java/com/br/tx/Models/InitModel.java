package com.br.tx.Models;

import java.util.ArrayList;
import java.util.List;

public class InitModel {

	private String scriptFolderDir;
	private String libFolderDir;
	private boolean haveLib;
	private String mainScriptName;
	private String mainScriptMessageClassName;
	private String jsonPayload;

	public String getJsonPayload() {
		return jsonPayload;
	}

	public void setJsonPayload(String jsonPayload) {
		this.jsonPayload = jsonPayload;
	}

	public String getScriptFolderDir() {
		return scriptFolderDir;
	}

	public String getLibFolderDir() {
		return libFolderDir;
	}

	public boolean isHaveLib() {
		return haveLib;
	}

	public String getMainScriptName() {
		return mainScriptName;
	}

	public String getMainScriptMessageClassName() {
		return mainScriptMessageClassName;
	}

	public void setMainScriptMessageClass(String mainScriptMessageClass) {
		this.mainScriptMessageClassName = mainScriptMessageClass;
	}

	public Object getMainScriptMessageClass() {
		try {
			String className = "com.br.tx.GroovyMessage." + this.mainScriptMessageClassName;
			Class<?> clazz = Class.forName(className);
			Object finalClass = clazz.getDeclaredConstructor().newInstance();
			return finalClass;
		} catch (Exception e) {
			return null;
		}
	}

	public String validModel() {
		List<String> logList = new ArrayList<String>();

		if (this.mainScriptName == null) {
			logList.add("mainScriptName nulo.");
		}

		if (this.jsonPayload == null) {
			logList.add("jsonPayload nulo.");
		}

		if (this.scriptFolderDir == null) {
			logList.add("scriptFolderDir nulo.");
		}

		if (this.mainScriptMessageClassName == null) {
			logList.add("mainScriptMessageClassName nulo.");
		} else {
			String className = "com.br.tx.GroovyMessage." + mainScriptMessageClassName;
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				logList.add(String.format("mainScriptMessageClassName '%s' inválido. Classe não encontrada.",
						mainScriptMessageClassName));
			}
		}

		if (haveLib == true && libFolderDir == null) {
			logList.add("libFolderDir nulo com haveLib 'true'.");
		}

		if (logList.isEmpty()) {
			return null;
		}

		return String.join(System.lineSeparator(), logList);
	}

	@Override
	public String toString() {
		return "InitModel [scriptFolderDir=" + scriptFolderDir + ", libFolderDir=" + libFolderDir + ", haveLib="
				+ haveLib + ", mainScriptName=" + mainScriptName + ", mainScriptMessageClassName="
				+ mainScriptMessageClassName + "]";
	}
}
