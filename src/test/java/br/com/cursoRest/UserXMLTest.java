package br.com.cursoRest;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.internal.path.xml.NodeImpl;


public class UserXMLTest {

	@Test
	public void devoTrabalharComXml() {		
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/usersXML/3")
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
	public void devoTrabalharComXmlNoRaiz() {		
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/usersXML/3")
		.then()
			.statusCode(200)
			.rootPath("user")//adiciona caminho padrao onde estao todos os dados XML, para nao ficar repetindo
			.body("name",is("Ana Julia"))
			.body("@id", is("3"))
			
			.rootPath("user.filhos")//adiciona caminho para nao ficar repetindo
			.body("name.size()",is(2))
			
			.detachRootPath("filhos")//remove caminho 
			.body("filhos.name[0]",is("Zezinho"))
			.body("filhos.name[1]",is("Luizinho"))
			.body("filhos.name",hasItem("Luizinho"))
			.body("filhos.name",hasItems("Zezinho","Luizinho"))
			;
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXML() {		
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.body("users.user.size()", is(3))
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("users.user.@id", hasItems("1","2","3"))
			.body("users.user.find{it.age.toInteger() == 25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina","Ana Julia"))
			.body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))
			.body("users.user.age.collect{it.toInteger() * 2}", hasItems(40,50,60))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
		;
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXMLComJava() {
		String path =
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().startsWith('Maria')}")
		;
		
		System.out.println(path.toString());
		
		Assert.assertEquals("Maria Joaquina",path.toString());
	}
	
	@Test
	public void devoFazerPesquisasAvancadasComXMLComJava2() {
		ArrayList<NodeImpl> nomes =
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}")
		;
		
		System.out.println(nomes);
		
		Assert.assertEquals(2,nomes.size());
		Assert.assertEquals("Maria Joaquina",nomes.get(0).toString());
		Assert.assertEquals("Ana Julia",nomes.get(1).toString());
	}
	
}
