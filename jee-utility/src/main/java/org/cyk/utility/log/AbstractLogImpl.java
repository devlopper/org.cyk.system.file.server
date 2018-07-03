package org.cyk.utility.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.collection.CollectionHelper;
import org.cyk.utility.function.AbstractFunctionWithVoidAsOutputImpl;
import org.cyk.utility.log.message.LogMessage;
import org.cyk.utility.log.message.LogMessageBuilder;

public abstract class AbstractLogImpl extends AbstractFunctionWithVoidAsOutputImpl<LogMessage> implements Log,Serializable {
	private static final long serialVersionUID = 8661933611010350759L;

	@Override
	public LogMessageBuilder getMessageBuilder(Boolean instanciateIfNull) {
		LogMessageBuilder messageBuilder = getMessageBuilder();
		if(messageBuilder == null && Boolean.TRUE.equals(instanciateIfNull)){
			messageBuilder = __inject__(LogMessageBuilder.class);
			messageBuilder.setParent(this);
			setMessageBuilder(messageBuilder);
		}
		return messageBuilder;
	}
	
	@Override
	public LogMessageBuilder getMessageBuilder() {
		LogMessageBuilder builder = null;
		if(getProperties().getMessage() instanceof Properties){
			builder = (LogMessageBuilder) ((Properties)getProperties().getMessage()).getBuilder();
		}else{
			
		}
		return builder;
	}
	
	@Override
	public Log setMessageBuilder(LogMessageBuilder builder) {
		if(getProperties().getMessage() == null){
			getProperties().setMessage(new Properties());
		}
		if(getProperties().getMessage() instanceof Properties){
			((Properties)getProperties().getMessage()).setBuilder(builder);
		}else{
			
		}
		return this;
	}
	
	protected abstract Object __getLevel__(LogLevel level);
	protected abstract Object __getMarker__(Collection<Object> markers);
	
	@Override
	public Log setThrowable(Throwable throwable) {
		getProperties().setThrowable(throwable);
		return this;
	}
	
	@Override
	public Throwable getThrowable() {
		return (Throwable) getProperties().getThrowable();
	}
	
	@Override
	public LogMessage getMessage() {
		return (LogMessage) getProperties().getMessage();
	}
	
	@Override
	public Log setMessage(LogMessage message) {
		getProperties().setMessage(message);
		return this;
	}
	
	@Override
	public LogLevel getLevel() {
		return (LogLevel) getProperties().getLogLevel();
	}
	
	@Override
	public Log setLevel(LogLevel level) {
		getProperties().setLogLevel(level);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Object> getMarkers() {
		return (Collection<Object>) getProperties().getMarkers();
	}
	
	@Override
	public Log setMarkers(Collection<Object> markers) {
		getProperties().setMarkers(markers);
		return this;
	}
	
	@Override
	public Log addMarkers(Collection<?> markers) {
		if(__inject__(CollectionHelper.class).isNotEmpty(markers)){
			Collection<Object> collection = getMarkers();
			if(collection == null)
				setMarkers(collection = new ArrayList<>());
			collection.addAll(markers);
		}
		return this;
	}
	
	@Override
	public Log addMarkers(String... markers) {
		addMarkers(__inject__(CollectionHelper.class).instanciate(markers));
		return this;
	}
	
	/**/
	
	@Override
	public Log execute(String message, LogLevel level) {
		setLevel(level).getMessageBuilder(Boolean.TRUE).addParameter(message).getParent().execute();
		return this;
	}
	
	@Override
	public Log executeInfo(String message) {
		return execute(message, LogLevel.INFO);
	}
	
	@Override
	public Log executeWarn(String message) {
		return execute(message, LogLevel.WARN);
	}
	
	@Override
	public Log executeTrace(String message) {
		return execute(message, LogLevel.TRACE);
	}
	
	@Override
	public Log executeDebug(String message) {
		return execute(message, LogLevel.DEBUG);
	}
	
	@Override
	public Log executeThrowable(Throwable throwable) {
		setLevel(LogLevel.ERROR).setThrowable(throwable).execute();
		return null;
	}
}
