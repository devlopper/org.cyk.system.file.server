package org.cyk.utility.common.cdi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Matcher;

import javax.annotation.PostConstruct;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.cyk.utility.common.CommonUtils;
import org.cyk.utility.common.RunnableListener;

public class AbstractBean implements Serializable {

	private static final long serialVersionUID = -2448439169984218703L;

	protected CommonUtils commonUtils = CommonUtils.getInstance();
	
	@Getter protected Collection<BeanListener> beanListeners = new ArrayList<>();
	
	//private Long __timestamp__;
	
	@PostConstruct
	public void postConstruct(){
		beforeInitialisation();
		initialisation();
		afterInitialisation();
	}
	
	protected void beforeInitialisation(){}
	
	protected void initialisation(){}

	protected void afterInitialisation(){}
	
	protected <T> T getReference(BeanManager aBeanManager,Class<T> aClass){
		Set<Bean<?>> beans = aBeanManager.getBeans(aClass);
		@SuppressWarnings("unchecked")
		Bean<T> bean = (Bean<T>) aBeanManager.resolve(beans);
		CreationalContext<T> context = aBeanManager.createCreationalContext(bean);
		@SuppressWarnings("unchecked")
		T result = (T) aBeanManager.getReference(bean, aClass, context);
		return result;
	}
	
	protected <T> T getReference(Class<T> aClass){
		return getReference(getBeanManager(), aClass);
	}
	
	protected BeanManager getBeanManager(){
		return CDI.current().getBeanManager();
	}
	
	protected <T> T newInstance(Class<T> aClass){
		try {
			return aClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected void pause(long millisecond){
		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**/
	
	protected String __info__(String message,int lineLength){
		return StringUtils.rightPad(message, lineLength);
	}
	
	protected void __writeInfo__(String message,int lineLength,String separator){
		if(separator==null)
			message = __info__(message, lineLength);
		else
			message = __info__(message, lineLength)+separator;
		for(BeanListener beanListener : beanListeners)
			beanListener.info(message);
	}
	
	protected void __writeInfo__(String message,int lineLength){
		__writeInfo__(message, lineLength, null);
	}
	
	protected void __writeInfoStart__(String message){
		__writeInfo__(message, 40, " : ");
	}
	
	protected void __writeInfoStartOK__(){
		__writeInfo__("OK");
	}
	
	protected void __writeInfo__(String message){
		__writeInfo__(message, 0, null);
	}
	
	protected void debug(Object object){
		System.out.println("------------------------------------- Debug -----------------------------");
		if(object instanceof Matcher){
			Matcher matcher = (Matcher) object;
			for(int i=1;i<=matcher.groupCount();i++)
				System.out.println("Group "+i+" = "+matcher.group(i));
		}else
			System.out.println(ToStringBuilder.reflectionToString(object, ToStringStyle.MULTI_LINE_STYLE));
	}
	
	protected void logStackTrace(){
		try {
			throw new RuntimeException("Stack Trace");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if(ArrayUtils.isEmpty(stackTraceElements)){
			System.out.println("No stack trace to log.");
			return;
		}
		StackTraceElement stackTraceElement = stackTraceElements[0];
		System.out.println(stackTraceElement.getClassName()+" - "+stackTraceElement.getMethodName()+" - "+stackTraceElement.getLineNumber());
		*/
	}
	
	/**/
	
	protected static <RUNNABLE extends Runnable> void runnableStarted(RUNNABLE aRunnable,Collection<RunnableListener<RUNNABLE>> listeners){
		Long time = System.currentTimeMillis();
		for(RunnableListener<RUNNABLE> listener : listeners)
			listener.started(aRunnable,time);
	}
	
	protected static <RUNNABLE extends Runnable> void runnableStopped(RUNNABLE aRunnable,Collection<RunnableListener<RUNNABLE>> listeners){
		Long time = System.currentTimeMillis();
		for(RunnableListener<RUNNABLE> listener : listeners)
			listener.stopped(aRunnable,time);
	}
	
	protected static <RUNNABLE extends Runnable> void runnableThrowable(RUNNABLE aRunnable,Collection<RunnableListener<RUNNABLE>> listeners,Throwable throwable){
		for(RunnableListener<RUNNABLE> listener : listeners)
			listener.throwable(aRunnable,throwable);
	}
	
	/**/
	
	protected Execution __watchExecute__(String name,Runnable aRunnable){
		Execution execution;
		execution = new Execution(name);
		execution.start();
		aRunnable.run();
		execution.end();
		return execution;
	}
	
	@Getter
	protected class Execution{
		private String name;
		private long startTimestamp;
		private long duration;
		
		public void start(){
			__writeInfoStart__(name);
			startTimestamp = System.currentTimeMillis();
		}
		
		public void end(){
			duration = System.currentTimeMillis()-startTimestamp;
			__writeInfoStartOK__();
		}

		public Execution(String name) {
			super();
			this.name = name;
		}
	}
}
