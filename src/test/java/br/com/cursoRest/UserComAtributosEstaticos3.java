package br.com.cursoRest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserComAtributosEstaticos3 {

	static RequestSpecification requestSpecification;
	static ResponseSpecification responseSpecification;
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";

		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		requestSpecification = reqBuilder.build();	
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		responseSpecification = resBuilder.build();
		
		RestAssured.requestSpecification = requestSpecification;
		RestAssured.responseSpecification = responseSpecification;
	}
	
	@Test
	public void usandoAtributosEstaticos5() {
		RestAssured
		.given()	
			//.spec(requestSpecification)
		.when()
			.get("/usersXML/3")
		.then()
			//.spec(responseSpecification)
			.body("user.name",is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.filhos.name.size()",is(2))
			.body("user.filhos.name[0]",is("Zezinho"))
			.body("user.filhos.name[1]",is("Luizinho"))
			.body("user.filhos.name",hasItem("Luizinho"))
			.body("user.filhos.name",hasItems("Zezinho","Luizinho"))
		;
	}
}
