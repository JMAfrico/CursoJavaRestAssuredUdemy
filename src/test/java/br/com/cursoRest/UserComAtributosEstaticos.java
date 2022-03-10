package br.com.cursoRest;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserComAtributosEstaticos {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
		//RestAssured.port = 443;
		//RestAssured.basePath = "/v2";
	}
	
	@Test
	public void usandoAtributosStaticos() {		
		RestAssured
		.given()
			.log().all()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
		;
	}
	
	@Test
	public void usandoAtributosStaticos2() {	
		RestAssured
		.given()	
			.log().all()
		.when()
			.get("/usersXML/3")
		.then()
			.statusCode(200)
			.body("user.name",is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.filhos.name.size()",is(2))
			.body("user.filhos.name[0]",is("Zezinho"))
			.body("user.filhos.name[1]",is("Luizinho"))
			.body("user.filhos.name",hasItem("Luizinho"))
			.body("user.filhos.name",hasItems("Zezinho","Luizinho"))
			;
	}
	
	@Test
	public void usandoAtributosStaticos3Xpath() {
		RestAssured
		.given()	
			.log().all()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.body(Matchers.hasXPath("count(/users/user)",Matchers.is("3")))//faz a contagem do XEndPoint via Xpath
			.body(Matchers.hasXPath("/users/user[@id=1]"))// Verifica de existe id = 1
			.body(Matchers.hasXPath("//user[@id=3]"))// Verifica de existe id = 3
			.body(Matchers.hasXPath("//name[text() = 'Zezinho']/../../name", Matchers.is("Ana Julia")))
			.body(Matchers.hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", Matchers.allOf(Matchers.containsString("Zezinho"))))
			.body(Matchers.hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", Matchers.allOf(Matchers.containsString("Zezinho"),Matchers.containsString("Luizinho"))))
			.body(Matchers.hasXPath("/users/user/name", Matchers.is("João da Silva")))
			.body(Matchers.hasXPath("//name", Matchers.containsString("João da Silva")))
			.body(Matchers.hasXPath("//user[last()]/name", Matchers.containsString("Ana Julia")))
			.body(Matchers.hasXPath("count(/users/user/name[contains(., 'n')])", Matchers.is("2")))
			.body(Matchers.hasXPath("//user[age < 24]/name", Matchers.is("Ana Julia")))
			.body(Matchers.hasXPath("//user[age > 20 and age < 30]/name", Matchers.is("Maria Joaquina")))
		;		
	}
	
	@Test
	public void usandoAtributosEstaticos4() {
		//Mostrar no log a requisicao
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		RequestSpecification requestSpecification = reqBuilder.build();
		
		RestAssured
		.given()	
			.spec(requestSpecification)
		.when()
			.get("/usersXML/3")
		.then()
			.statusCode(200)
			.body("user.name",is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.filhos.name.size()",is(2))
			.body("user.filhos.name[0]",is("Zezinho"))
			.body("user.filhos.name[1]",is("Luizinho"))
			.body("user.filhos.name",hasItem("Luizinho"))
			.body("user.filhos.name",hasItems("Zezinho","Luizinho"))
		;
	}
	
	@Test
	public void usandoAtributosEstaticos5() {
		//Mostrar no given a requisicao
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);
		RequestSpecification requestSpecification = reqBuilder.build();
		
		//Mostrar no then a resposta
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		ResponseSpecification responseSpecification = resBuilder.build();
		
		RestAssured
		.given()	
			.spec(requestSpecification)
		.when()
			.get("/usersXML/3")
		.then()
			.spec(responseSpecification)
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
