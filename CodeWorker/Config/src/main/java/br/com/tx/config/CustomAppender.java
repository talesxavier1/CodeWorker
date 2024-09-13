package br.com.tx.config;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;

@Plugin(name = "CustomAppender", category = "Core", elementType = Appender.ELEMENT_TYPE)
public class CustomAppender extends AbstractAppender {

    protected CustomAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout, true);
    }

    @PluginFactory
    public static CustomAppender createAppender(@PluginAttribute("name") String name,
                                                @PluginElement("Filter") Filter filter,
                                                @PluginElement("Layout") Layout<? extends Serializable> layout) {
        return new CustomAppender(name, filter, layout);
    }

    @Override
    public void append(LogEvent event) {
        // Aqui você pode definir o que fazer com os logs
        String logMessage = new String(getLayout().toByteArray(event));
        // Por exemplo, armazenar em uma lista ou enviar para um serviço externo
        System.out.println(logMessage); // Apenas um exemplo, substitua com sua lógica
    }
}
