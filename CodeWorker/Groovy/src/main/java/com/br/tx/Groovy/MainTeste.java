package com.br.tx.Groovy;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class MainTeste {
	public static void main(String[] args) throws CompilationFailedException, IOException, ClassNotFoundException {

		Binding binding = new Binding();
		GroovyClassLoader classLoader = new GroovyClassLoader();

		classLoader.addClasspath("C:\\Users\\tales\\OneDrive\\Documentos\\C_Sharp\\MOCK\\src\\lib\\gson-2.10.1.jar");

		GroovyShell shell = new GroovyShell(classLoader, binding);

		Script scrpt = shell
				.parse(new File("C:\\Users\\tales\\OneDrive\\Documentos\\C_Sharp\\MOCK\\src\\main\\main.groovy"));

		String aa = "";
	}
}
