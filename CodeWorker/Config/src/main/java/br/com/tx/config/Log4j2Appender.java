package br.com.tx.config;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "Log4j2Appender", category = "Core", elementType = "appender", printObject = true)
public class Log4j2Appender extends AbstractAppender {


	@SuppressWarnings("deprecation")
	protected Log4j2Appender(String name, Filter filter, final Layout<? extends Serializable> layout) {
		super(name, filter, layout);
	}

	@SuppressWarnings("unchecked")
	@PluginFactory
	public static Log4j2Appender createAppender(@PluginAttribute("name") String name,
			@SuppressWarnings("rawtypes") @PluginElement("Layout") Layout layout,
			@PluginElement("Filters") Filter filter) {
		return new Log4j2Appender(name, filter, layout);
	}

	@Override
	public void append(LogEvent event) {
		String logMessage = new String(getLayout().toByteArray(event));
		System.out.println("append \n" + logMessage);
	}
}
