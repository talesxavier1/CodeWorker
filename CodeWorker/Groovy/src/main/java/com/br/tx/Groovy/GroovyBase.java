package com.br.tx.Groovy;

import java.io.File;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.tx.Models.InitModel;
import com.google.gson.Gson;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class GroovyBase {
	static final Logger logger = LogManager.getLogger(GroovyBase.class);

	private File scriptFolder;
	private File libFolder;
	private File mainGroovyScriptFile;
	private Object groovyMessage;

	private boolean everythingOk = true;

	public GroovyBase(InitModel initModel) {
		validAndSetScriptPathFile(initModel.getScriptFolderDir());
		if (initModel.isHaveLib()) {
			validAndSetLibPathFile(initModel.getLibFolderDir());
		}
		validAndSetMainGroovyScriptFile(initModel.getMainScriptName());

		Gson gson = new Gson();
		this.groovyMessage = gson.fromJson(initModel.getJsonPayload(),
				initModel.getMainScriptMessageClass().getClass());
	}

	public Object executeScript(Object groovyMessage) {
		if (this.everythingOk == false) {
			logger.warn("Classe GroovyBase carregada com erro. Não será posível executar função 'executeScript'.");
		}

		Binding binding = new Binding();
		GroovyClassLoader classLoader = new GroovyClassLoader();

		for (File file : this.libFolder.listFiles()) {
			classLoader.addClasspath(file.getPath());
		}
		for (File file : this.scriptFolder.listFiles()) {
			if (!file.getName().equals(this.mainGroovyScriptFile.getName())) {
				classLoader.addClasspath(file.getPath());
			}
		}

		GroovyShell shell = new GroovyShell(classLoader, binding);
		Script script;
		try {
			script = shell.parse(this.mainGroovyScriptFile);
		} catch (Exception e) {
			logger.fatal("Erro ao fazer o parse do mainGroovyScriptFile", e);
			return null;
		}

		Object result = script.invokeMethod("processData", groovyMessage);
		if (result.getClass().getName() != groovyMessage.getClass().getName()) {
			var logList = new ArrayList<String>();
			logList.add("Script executado, mas classe de retorno não foi a mesma enviada como parâmetro.");
			logList.add(String.format("Classe enviada como parâmetro:%s", groovyMessage.getClass().getName()));
			logList.add(String.format("Classe de retorno:%s", result.getClass().getName()));
			logger.fatal(String.join(System.lineSeparator(), logList));
			return null;
		}
		String a = result.getClass().getName();
//		if(result.getClass().getName())
		System.out.println("");

		Gson gson = new Gson();
		String saida = gson.toJson(result);
		System.out.println("SAIDA =>> " + saida);
		return result;
	}

	private void validAndSetScriptPathFile(String scriptPath) {
		File folder = new File(scriptPath);
		boolean isValid = validFileFolder(folder);
		if (isValid == false) {
			logger.fatal(String.format("Diretório '%s' não encontrado ou vazio.", scriptPath));
			this.everythingOk = false;
			return;
		}

		this.scriptFolder = folder;
	}

	private void validAndSetLibPathFile(String libPath) {
		File folder = new File(libPath);
		boolean isValid = validFileFolder(folder);
		if (isValid == false) {
			logger.fatal(String.format("Diretório '%s' não encontrado ou vazio.", libPath));
			this.everythingOk = false;
			return;
		}

		this.libFolder = folder;
	}

	private void validAndSetMainGroovyScriptFile(String mainGroovyScriptName) {
		if (everythingOk == false) {
			return;
		}
		File file = new File(this.scriptFolder.getPath() + File.separator + mainGroovyScriptName);
		if (file.exists() == false) {
			logger.fatal(String.format("Diretório '%s' Não possui o arquivo '%s'.", this.scriptFolder.getPath(),
					mainGroovyScriptName));
			this.everythingOk = false;
			return;
		}
		this.mainGroovyScriptFile = file;
	}

	private boolean validFileFolder(File folder) {
		if (!folder.exists() || !folder.isDirectory()) {
			return false;
		}

		File[] listOfFiles = folder.listFiles();
		if (listOfFiles.length == 0) {
			return false;
		}
		return true;
	}

	public boolean isEverythingOk() {
		return everythingOk;
	}

	public Object getGroovyMessage() {
		return groovyMessage;
	}
}
