package org.cyk.utility.representation.server;

import java.io.Serializable;

import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.__kernel__.rest.ResponseBuilder;
import org.cyk.utility.business.TransactionResult;
import org.cyk.utility.representation.SpecificRepresentation;

public abstract class AbstractSpecificRepresentationImpl<ENTITY> extends AbstractObject implements SpecificRepresentation<ENTITY>,Serializable {

	protected static void processTransactionResult(TransactionResult transactionResult,ResponseBuilder.Arguments responseBuilderArguments) {
		if(transactionResult == null || responseBuilderArguments == null)
			return;
		responseBuilderArguments.setEntity(transactionResult.buildSuccessMessage());
		responseBuilderArguments.setHeader(Action.CREATE.name(), transactionResult.getNumberOfCreation());
		responseBuilderArguments.setHeader(Action.UPDATE.name(), transactionResult.getNumberOfUpdate());
		responseBuilderArguments.setHeader(Action.DELETE.name(), transactionResult.getNumberOfDeletion());
	}
	
	/**/
	
	public static abstract class AbstractRunnableImpl extends AbstractObject implements Runnable,Serializable {
		
		protected ResponseBuilder.Arguments responseBuilderArguments;
		
		public AbstractRunnableImpl(ResponseBuilder.Arguments responseBuilderArguments) {
			this.responseBuilderArguments = responseBuilderArguments;
		}
		
		@Override
		public void run() {
			__run__();
		}
		
		protected abstract void __run__();
		
		public static abstract class TransactionImpl extends AbstractRunnableImpl implements Serializable {
			
			public TransactionImpl(ResponseBuilder.Arguments responseBuilderArguments) {
				super(responseBuilderArguments);
			}
			
			@Override
			protected void __run__() {
				TransactionResult result = transact();
				processTransactionResult(result, responseBuilderArguments);
			}
			
			protected abstract TransactionResult transact();
		}
	}
}