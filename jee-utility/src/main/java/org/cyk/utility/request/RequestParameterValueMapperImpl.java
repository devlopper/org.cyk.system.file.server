package org.cyk.utility.request;

import java.io.Serializable;

import org.cyk.utility.field.FieldName;
import org.cyk.utility.function.AbstractFunctionWithPropertiesAsInputImpl;
import org.cyk.utility.identifier.resource.UniformResourceIdentifierParameterNameStringBuilder;
import org.cyk.utility.identifier.resource.UniformResourceIdentifierParameterValueMatrix;
import org.cyk.utility.identifier.resource.UniformResourceIdentifierParameterValueStringBuilder;
import org.cyk.utility.number.NumberHelper;
import org.cyk.utility.object.ObjectByStringMap;
import org.cyk.utility.system.action.SystemAction;
import org.cyk.utility.system.action.SystemActionCreate;
import org.cyk.utility.system.action.SystemActionDelete;
import org.cyk.utility.system.action.SystemActionRead;
import org.cyk.utility.system.action.SystemActionUpdate;

public class RequestParameterValueMapperImpl extends AbstractFunctionWithPropertiesAsInputImpl<Object> implements RequestParameterValueMapper,Serializable {
	private static final long serialVersionUID = 1L;
	
	private Object parameterName;
	private String parameterValue;
	private ObjectByStringMap parameters;
	
	@Override
	protected Object __execute__() throws Exception {
		Object value = null;
		Object parameterName = getParameterName();
		if(parameterName!=null) {
			String parameterValue = getParameterValue();
			if(parameterValue == null) {
				parameterName = __inject__(UniformResourceIdentifierParameterNameStringBuilder.class).setName(parameterName).execute().getOutput();
				parameterValue = __inject__(RequestParameterValueGetter.class).setParameterName(parameterName).execute().getOutputAs(String.class);
			
			}
			/*
			if(parameterValue == null) {
				ObjectByStringMap parameters = getParameters();
				if(parameters!=null) {
					Object object = parameters.get(parameterName);
					if(object instanceof String)
						parameterValue = object.toString();
					else
						__injectThrowableHelper__().throwRuntimeException("Multiple parameter value not yet handled");
				}
			}
			*/
			if(__injectStringHelper__().isNotBlank(parameterValue)) {
				if(__inject__(UniformResourceIdentifierParameterNameStringBuilder.class).setName(SystemAction.class).execute().getOutput().equals(parameterName)) {
					if(__inject__(UniformResourceIdentifierParameterValueStringBuilder.class).setValue(SystemActionCreate.class).execute().getOutput().equals(parameterValue))
						value = __inject__(SystemActionCreate.class);
					else if(__inject__(UniformResourceIdentifierParameterValueStringBuilder.class).setValue(SystemActionRead.class).execute().getOutput().equals(parameterValue))
						value = __inject__(SystemActionRead.class);
					else if(__inject__(UniformResourceIdentifierParameterValueStringBuilder.class).setValue(SystemActionUpdate.class).execute().getOutput().equals(parameterValue))
						value = __inject__(SystemActionUpdate.class);
					else if(__inject__(UniformResourceIdentifierParameterValueStringBuilder.class).setValue(SystemActionDelete.class).execute().getOutput().equals(parameterValue))
						value = __inject__(SystemActionDelete.class);
				}else if(__inject__(UniformResourceIdentifierParameterNameStringBuilder.class).setName(FieldName.IDENTIFIER).execute().getOutput().equals(parameterName)) {
					value = __inject__(NumberHelper.class).getLong(parameterValue);
				}else if(__inject__(UniformResourceIdentifierParameterNameStringBuilder.class).setName(Class.class).execute().getOutput().equals(parameterName)) {
					value = __injectCollectionHelper__().getFirst(__inject__(UniformResourceIdentifierParameterValueMatrix.class).getClassMap().getKeys(parameterValue));
				}
				
				if(value == null)
					__injectThrowableHelper__().throwRuntimeException("request parameter "+parameterName+":"+parameterValue+" cannot be mapped.");
			}
		}
		return value;
	}
	
	@Override
	public Object getParameterName() {
		return parameterName;
	}

	@Override
	public RequestParameterValueMapper setParameterName(Object parameterName) {
		this.parameterName = parameterName;
		return this;
	}

	@Override
	public String getParameterValue() {
		return parameterValue;
	}

	@Override
	public RequestParameterValueMapper setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
		return this;
	}

	@Override
	public ObjectByStringMap getParameters() {
		return parameters;
	}
	
	@Override
	public ObjectByStringMap getParameters(Boolean injectIfNull) {
		return (ObjectByStringMap) __getInjectIfNull__(FIELD_PARAMETERS, injectIfNull);
	}

	@Override
	public RequestParameterValueMapper setParameters(ObjectByStringMap parameters) {
		this.parameters = parameters;
		return this;
	}

	public static final String FIELD_PARAMETERS = "parameters";
	
}
