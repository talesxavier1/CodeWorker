package com.br.tx.Groovy;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.tx.Models.InitModel;

public class GroovyBase {
	static final Logger logger = LogManager.getLogger(GroovyBase.class);
	private String oSName;
	private Character jarFileSeparator;
	private File scriptDirFile;
	private File libDirFile;

	public GroovyBase(InitModel initModel) {
		this.oSName = System.getProperty("os.name");
		this.jarFileSeparator = getFileSeparator(this.oSName);
		setScriptPathFile(initModel.getScriptDir());
		if (initModel.isHaveLib()) {
			setLibPathFile(initModel.getLibDir());
		}

	}

	private Character getFileSeparator(String osName) {
		String lOsName = osName.toLowerCase();
		if (lOsName.contains("linux")) {
			return ':';
		}
		if (lOsName.contains("windows")) {
			return ';';
		}

		logger.warn(String.format("Não foi possível definir FileSeparator para osName %s. Valor padrão definido ':'.",
				osName));
		return ':';
	}

	private void setScriptPathFile(String scriptPath) {
		File file = new File(scriptPath);
		this.scriptDirFile = file;
		String a = "";

	}

	private void setLibPathFile(String libPath) {
		File file = new File(libPath);
		this.libDirFile = file;
		String a = "";
	}
}
