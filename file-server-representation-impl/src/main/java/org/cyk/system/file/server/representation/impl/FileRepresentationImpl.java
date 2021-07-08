package org.cyk.system.file.server.representation.impl;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.cyk.system.file.server.business.api.FileBusiness;
import org.cyk.system.file.server.business.impl.FileBusinessImpl;
import org.cyk.system.file.server.persistence.api.query.FileQuerier;
import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.api.FileRepresentation;
import org.cyk.system.file.server.representation.entities.FileDto;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.configuration.ConfigurationHelper;
import org.cyk.utility.__kernel__.constant.ConstantString;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.rest.RequestProcessor;
import org.cyk.utility.__kernel__.runnable.Runner;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.business.TransactionResult;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.representation.Arguments;
import org.cyk.utility.representation.EntityReader;
import org.cyk.utility.representation.server.AbstractSpecificRepresentationImpl;

@ApplicationScoped
public class FileRepresentationImpl extends AbstractSpecificRepresentationImpl<FileDto> implements FileRepresentation,Serializable {
	private static final long serialVersionUID = 1L;

	@Inject private FileBusiness fileBusiness;
	
	@Override
	public Response get(String filterAsString,Boolean countable,Integer firstTupleIndex,Integer numberOfTuples) {
		Arguments arguments = new Arguments().setRepresentationEntityClass(FileDto.class).setPersistenceEntityClass(File.class)
				.setCountable(countable);
		arguments.getQueryExecutorArguments(Boolean.TRUE).setQueryIdentifier(FileQuerier.QUERY_IDENTIFIER_READ_DYNAMIC);
		if(StringHelper.isNotBlank(filterAsString))
			arguments.getQueryExecutorArguments(Boolean.TRUE).addFilterFieldsValues(FileQuerier.PARAMETER_NAME_NAME,filterAsString);
		arguments.getQueryExecutorArguments(Boolean.TRUE).setFirstTupleIndex(firstTupleIndex).setNumberOfTuples(numberOfTuples);
		arguments.getResponseBuilderArguments(Boolean.TRUE).setHeadersCORS();
		return EntityReader.getInstance().read(arguments);
	}
	
	@Override
	public Response import_(List<String> pathsNames, String acceptedPathNameRegularExpression) {
		Runner.Arguments runnerArguments = new Runner.Arguments();
		return RequestProcessor.getInstance().process(new RequestProcessor.Request.AbstractImpl() {
			@Override
			public Runner.Arguments getRunnerArguments() {
				return runnerArguments;
			}
			@Override
			public Runnable getRunnable() {
				return new AbstractRunnableImpl.TransactionImpl(responseBuilderArguments){
					@Override
					public TransactionResult transact() {
						TransactionResult result = fileBusiness.import_(CollectionHelper.isEmpty(pathsNames) 
								? ConfigurationHelper.getValueAsStrings(FileBusinessImpl.FILES_PATHS_NAMES) : pathsNames
								, StringHelper.isBlank(acceptedPathNameRegularExpression) 
								? ConfigurationHelper.getValueAsString(FileBusinessImpl.ACCEPTED_PATH_NAME_REGULAR_EXPRESSION) : acceptedPathNameRegularExpression);
						if(Boolean.TRUE.equals(NumberHelper.isGreaterThanZero(result.getNumberOfCreation())))
							responseBuilderArguments.setStatus(Status.CREATED);
						return result;
					}
				};
			}
		});
	}
	
	@Override
	public Response extractBytesOfAll() {
		Runner.Arguments runnerArguments = new Runner.Arguments();
		return RequestProcessor.getInstance().process(new RequestProcessor.Request.AbstractImpl() {
			@Override
			public Runner.Arguments getRunnerArguments() {
				return runnerArguments;
			}
			@Override
			public Runnable getRunnable() {
				return new AbstractRunnableImpl.TransactionImpl(responseBuilderArguments){			
					@Override
					public TransactionResult transact() {
						return fileBusiness.extractBytesOfAll();						
					}
				};
			}
		});
	}
	
	@Override
	public Response extractBytes(List<String> identifiers) {
		Runner.Arguments runnerArguments = new Runner.Arguments();
		return RequestProcessor.getInstance().process(new RequestProcessor.Request.AbstractImpl() {
			@Override
			public Runner.Arguments getRunnerArguments() {
				return runnerArguments;
			}
			@Override
			public Runnable getRunnable() {
				return new AbstractRunnableImpl.TransactionImpl(responseBuilderArguments){			
					@Override
					public TransactionResult transact() {
						return fileBusiness.extractBytesFromIdentifiers(identifiers);						
					}
				};
			}
		});
	}
	
	@Override
	public Response download(String identifier,Boolean isInline) {
		Runner.Arguments runnerArguments = new Runner.Arguments();
		return RequestProcessor.getInstance().process(new RequestProcessor.Request.AbstractImpl() {
			@Override
			public Runner.Arguments getRunnerArguments() {
				return runnerArguments;
			}
			@Override
			public Runnable getRunnable() {
				return new Runnable() {					
					@Override
					public void run() {
						File file = fileBusiness.download(identifier);
						responseBuilderArguments.setEntity(file.getBytes());
						responseBuilderArguments.setHeader(HttpHeaders.CONTENT_TYPE, file.getMimeType());
					    String name = FileHelper.concatenateNameAndExtension(file.getName(), file.getExtension());
					    responseBuilderArguments.setHeader(HttpHeaders.CONTENT_DISPOSITION, (Boolean.TRUE.equals(isInline) ? ConstantString.INLINE : ConstantString.ATTACHMENT)+"; "+ConstantString.FILENAME
					    		+"="+name);
					    if(NumberHelper.isGreaterThanZero(file.getSize()))
					    	responseBuilderArguments.setHeader(HttpHeaders.CONTENT_LENGTH, file.getSize());
					    responseBuilderArguments.setHeadersCORS();
					}
				};
			}
			
			@Override
			public Response getResponseWhenThrowableIsNotNull(Runner.Arguments arguments) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
		});
	}
}