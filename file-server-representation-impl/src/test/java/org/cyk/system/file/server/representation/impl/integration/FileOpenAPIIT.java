package org.cyk.system.file.server.representation.impl.integration;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItems;

import java.nio.charset.Charset;

import org.cyk.system.file.server.representation.impl.openapi.FileOpenAPI;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.jboss.arquillian.junit.InSequence;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class FileOpenAPIIT extends AbstractClientIT {

	@BeforeClass
	public static void listenBeforeClass() {
		RestAssured.basePath = "test/api/"+FileOpenAPI.PATH;
		RestAssured.config = config().encoderConfig(EncoderConfig.encoderConfig().defaultCharsetForContentType(Charset.forName("UTF-8"), ContentType.URLENC));
	}
	
	@Test @InSequence(1)
    public void get() {
		Response response = given().when().get(FileOpenAPI.OPERATION_GET);
		response.then().statusCode(200).assertThat().body("nameAndExtension", hasItems("Beni soit la Sainte Trinité (couplets).pdf"));
    }
	
    @Test @InSequence(2)
    public void import_() {
    	Response response = given().contentType(ContentType.URLENC).formParam("nom", "komenan").when().post(FileOpenAPI.OPERATION_IMPORT);
    	response.then().statusCode(201);
    	assertThat(response.getHeader(Action.CREATE.name())).isEqualTo("1");
    	assertThat(response.getHeader(Action.UPDATE.name())).isEqualTo(null);
    	assertThat(response.getHeader(Action.DELETE.name())).isEqualTo(null);
    }
}