package org.cyk.system.file.server.representation.impl.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

import org.cyk.system.file.server.persistence.entities.File;
import org.cyk.system.file.server.representation.impl.openapi.FileOpenAPI;
import org.cyk.utility.__kernel__.rest.ResponseHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.representation.EntityReader;
import org.cyk.utility.test.arquillian.AbstractClientTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public abstract class AbstractClientIT extends AbstractClientTest {

	@Deployment
    public static Archive<?> buildArchive() {
    	return AbstractIT.buildArchive();
    }
	
	protected void assertGet(String filterAsString,Integer firstTupleIndex,Integer numberOfTuples,String expectedCollectionSize,String[] expectedNameAndExtension) {
		RequestSpecification requestSpecification = given().when();
		if(StringHelper.isNotBlank(filterAsString))
			requestSpecification.queryParam(EntityReader.PARAMETER_NAME_FILTER_AS_STRING, filterAsString);
		if(firstTupleIndex != null)
			requestSpecification.queryParam(EntityReader.PARAMETER_NAME_FIRST_TUPLE_INDEX, firstTupleIndex);
		if(numberOfTuples != null)
			requestSpecification.queryParam(EntityReader.PARAMETER_NAME_NUMBER_OF_TUPLES, numberOfTuples);		
		Response response = requestSpecification.get(FileOpenAPI.OPERATION_GET);
		response.then().statusCode(200).assertThat().body(File.FIELD_NAME_AND_EXTENSION, hasItems(expectedNameAndExtension));
		assertThat(response.getHeader(ResponseHelper.HEADER_COLLECTION_SIZE)).isEqualTo(expectedCollectionSize);
    }
}