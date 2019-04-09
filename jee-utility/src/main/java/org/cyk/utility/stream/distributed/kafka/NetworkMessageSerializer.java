package org.cyk.utility.stream.distributed.kafka;

import java.io.Serializable;
import java.util.Map;

import org.cyk.utility.__kernel__.annotation.Json;
import org.cyk.utility.__kernel__.object.dynamic.AbstractObject;
import org.cyk.utility.__kernel__.object.dynamic.Objectable;
import org.cyk.utility.object.ObjectToStringBuilder;

public class NetworkMessageSerializer extends AbstractObject implements org.apache.kafka.common.serialization.Serializer<org.cyk.utility.network.message.Message>,Objectable,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {

	}

	@Override
	public byte[] serialize(String arg0, org.cyk.utility.network.message.Message message) {
		return __injectByQualifiersClasses__(ObjectToStringBuilder.class, Json.Class.class).setObject(message)
				.addFieldNamesStrings(org.cyk.utility.network.message.Message.PROPERTY_TITLE,
						org.cyk.utility.network.message.Message.PROPERTY_BODY,org.cyk.utility.network.message.Message.PROPERTY_RECEIVERS)
				.execute().getOutput().getBytes();
	}

}