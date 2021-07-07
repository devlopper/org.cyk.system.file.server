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
import org.cyk.utility.__kernel__.string.Strings;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.business.TransactionResult;
import org.cyk.utility.file.FileHelper;
import org.cyk.utility.number.Intervals;
import org.cyk.utility.representation.server.AbstractSpecificRepresentationImpl;

@ApplicationScoped
public class FileRepresentationImpl extends AbstractSpecificRepresentationImpl<FileDto> implements FileRepresentation,Serializable {
	private static final long serialVersionUID = 1L;

	@Inject private FileBusiness fileBusiness;
	
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
	public Response extractBytes() {
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
						TransactionResult transactionResult = fileBusiness.extractBytes();
						if(transactionResult == null)
							return;
						runnerArguments.setResult(String.format("%s file(s) byte(s) extracted.", ValueHelper.defaultToIfNull(transactionResult.getNumberOfCreation(),0)));						
					}
				};
			}
		});
	}
	
	//@Override
	public Response createFromDirectories(List<String> directories,/*List<String> mimeTypeTypes,List<String> mimeTypeSubTypes,List<String> mimeTypes,*/List<String> extensions
			,List<String> sizes,Integer batchSize,Integer count) {
		Intervals intervals = null;
		if(CollectionHelper.isNotEmpty(sizes)) {
			intervals = __inject__(Intervals.class);
			for(String index : sizes) {
				String[] extremities = index.split(";");
				if(extremities.length == 2) {
					intervals.add(NumberHelper.getInteger(extremities[0]), NumberHelper.getInteger(extremities[1]));
				}
			}
		}
		__inject__(FileBusiness.class).createFromDirectories(__inject__(Strings.class).add(directories),null,null,null
				,__inject__(Strings.class).add(extensions),intervals,batchSize,count == null || count == 0 ? null : count);
		return Response.ok("Files has been created from directories").build();
	}
	
	//@Override
	public Response getManyByGlobalFilter(Boolean isPageable, Long from, Long count, String fields,String globalFilter,Boolean loggableAsInfo) {
		/*Arguments arguments = new Arguments().setRepresentationEntityClass(FileDto.class);
		arguments.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(FileQuerier.QUERY_IDENTIFIER_READ_VIEW_01)
				.addFilterField(File.FIELD_NAME, globalFilter)
				.setFirstTupleIndex(NumberHelper.getInteger(from))
				.setNumberOfTuples(NumberHelper.getInteger(count))
				).setCountable(Boolean.TRUE).setLoggableAsInfo(loggableAsInfo);
		return EntityReader.getInstance().read(arguments);
		//return getMany(null,isPageable, from, count, fields, new Filter.Dto().setValue(globalFilter));
		*/
		return null;
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
					}
				};
			}
		});
	}
}