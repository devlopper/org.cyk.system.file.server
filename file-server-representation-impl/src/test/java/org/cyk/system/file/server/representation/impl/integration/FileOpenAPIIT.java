package org.cyk.system.file.server.representation.impl.integration;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

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
		assertGet(null, null, null, "10", new String[] {"Beni soit la Sainte Trinité (couplets).pdf"});
    }
	
	@Test @InSequence(1)
    public void get_page() {
		assertGet(null, 0, 1, "1", new String[] {"Beni soit la Sainte Trinité (couplets).pdf"});
    }
	
	@Test @InSequence(1)
    public void get_filter_Sainte() {
		assertGet("Sainte", null, null, "1", new String[] {"Beni soit la Sainte Trinité (couplets).pdf"});
    }
	
	@Test @InSequence(1)
    public void get_filter_sainte() {
		assertGet("sainte", null, null, "1", new String[] {"Beni soit la Sainte Trinité (couplets).pdf"});
    }
	
	@Test @InSequence(1)
    public void get_filter_croix() {
		assertGet("croix", null, null, "2", new String[] {"Chant à la croix.txt"});
    }
	
	@Test @InSequence(2)
    public void download() {
		assertDownload("002aadb8-2f00-4bda-8e41-67e7938eed2b", "Chant à la croix", "txt","text/plain", 2l, "hello world!");
    }
	
    @Test @InSequence(3)
    public void import_() {
    	Response response = given().contentType(ContentType.URLENC).when().post(FileOpenAPI.OPERATION_IMPORT);
    	response.then().statusCode(201);
    	assertThat(response.getHeader(Action.CREATE.name())).isEqualTo("1");
    	assertThat(response.getHeader(Action.UPDATE.name())).isEqualTo(null);
    	assertThat(response.getHeader(Action.DELETE.name())).isEqualTo(null);
    }
}