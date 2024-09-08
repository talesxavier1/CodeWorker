package com.br.tx.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InitModel {

	private String scriptDir;
	private String libDir;
	private boolean haveLib;
	private String mainScriptName;

	public String getScriptDir() {
		return scriptDir;
	}

	public String getLibDir() {
		return libDir;
	}

	public boolean isHaveLib() {
		return haveLib;
	}

	public String getMainScriptName() {
		return mainScriptName;
	}

	public String validModel() {
		List<String> logList = new ArrayList<String>();

		if (this.mainScriptName == null) {
			logList.add("mainScriptName nulo.");
		}

		if (this.scriptDir == null) {
			logList.add("scriptDir nulo.");
		}

		if (haveLib == true && libDir == null) {
			logList.add("libDir nulo com haveLib 'true'.");
		}

		if (logList.isEmpty()) {
			return null;
		}

		return String.join(System.lineSeparator(), logList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(haveLib, libDir, mainScriptName, scriptDir);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InitModel other = (InitModel) obj;
		return haveLib == other.haveLib && Objects.equals(libDir, other.libDir)
				&& Objects.equals(mainScriptName, other.mainScriptName) && Objects.equals(scriptDir, other.scriptDir);
	}

	@Override
	public String toString() {
		return "InitModel [scriptDir=" + scriptDir + ", libDir=" + libDir + ", haveLib=" + haveLib + ", mainScriptName="
				+ mainScriptName + ", getScriptDir()=" + getScriptDir() + ", getLibDir()=" + getLibDir()
				+ ", isHaveLib()=" + isHaveLib() + ", getMainScriptName()=" + getMainScriptName() + ", hashCode()="
				+ hashCode() + ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}
}
