package org.cyk.utility.__kernel__.log;

import java.util.Collection;
import java.util.logging.Level;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.string.StringHelper;

public interface LogHelper {

	static String concatenateMessageTemplateWith(String template,String string) {
		if(string == null)
			return template;
		if(template == null)
			template = ConstantEmpty.STRING;
		if(!template.isEmpty())
			template = template + " ";
		template = template + string;
		return template;
	}
	
	static String concatenateMessageTemplateWithArgument(String template,String name) {
		if(StringHelper.isBlank(name))
			return template;
		return concatenateMessageTemplateWith(template, name + "=%s");
	}
	
	static void addStringToMessage(LogMessage message,String string) {
		if(message == null || string == null)
			return;
		message.setTemplate(concatenateMessageTemplateWith(message.getTemplate(), string));
	}
	
	static void addArgumentToMessage(LogMessage message,String name,Object value) {
		if(message == null || StringHelper.isBlank(name))
			return;
		message.setTemplate(concatenateMessageTemplateWithArgument(message.getTemplate(), name));
		message.getArguments(Boolean.TRUE).add(value);
	}
	
	static String buildMessage(String template,Collection<Object> arguments) {
		if(StringHelper.isBlank(template))
			return null;
		if(CollectionHelper.isEmpty(arguments))
			return template;
		return String.format(template, arguments.toArray());
	}
	
	static String buildMessage(String template,Object...arguments) {
		if(StringHelper.isBlank(template))
			return null;
		if(ArrayHelper.isEmpty(arguments))
			return template;
		return buildMessage(template, CollectionHelper.listOf(arguments));
	}
	
	static String buildMessage(LogMessage message) {
		if(message == null)
			return null;
		return buildMessage(message.getTemplate(), message.getArguments());
	}
	
	static void log(String message,Level level,Class<?> klass) {
		if(message == null || message.isBlank())
			return;
		if(level == null)
			level = Level.INFO;
		if(klass == null)
			klass = LogHelper.class;
		java.util.logging.Logger.getLogger(klass.getName()).log(level, message);
	}
	
	static void log(String message,Level level) {
		log(message, level, LogHelper.class);
	}
	
	static void log(String message) {
		log(message, Level.INFO, LogHelper.class);
	}
	
	static void logFinest(String message,Class<?> klass) {
		log(message, Level.FINEST,klass);
	}
	
	static void logFiner(String message,Class<?> klass) {
		log(message, Level.FINER,klass);
	}
	
	static void logFine(String message,Class<?> klass) {
		log(message, Level.FINE,klass);
	}

	static void logInfo(String message,Class<?> klass) {
		log(message, Level.INFO,klass);
	}
	
	static void logWarning(String message,Class<?> klass) {
		log(message, Level.WARNING,klass);
	}
	
	static void logSevere(String message,Class<?> klass) {
		log(message, Level.SEVERE,klass);
	}
	
	static void logAll(String message,Class<?> klass) {
		log(message, Level.ALL,klass);
	}
	
	static void logConfig(String message,Class<?> klass) {
		log(message, Level.CONFIG,klass);
	}
	
	static void logOff(String message,Class<?> klass) {
		log(message, Level.OFF,klass);
	}
	
	static void log(Throwable throwable,Class<?> klass) {
		logSevere(throwable.toString(),klass);
	}

	static void log(LogMessage message,Level level,Class<?> klass) {
		if(message == null)
			return;
		String string = buildMessage(message);
		if(StringHelper.isBlank(string))
			return;
		log(string,level,klass);
	}
	
}
