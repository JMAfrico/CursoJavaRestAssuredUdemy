package br.com.cursoRest;

import java.util.ArrayList;
import java.util.Arrays;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.hamcrest.xml.HasXPath;
import org.junit.Assert;
import org.junit.Test;

import groovyjarjarantlr4.v4.runtime.tree.xpath.XPath;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {

	@Test
	public void testeUsuario() {		
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(1))
			.body("name",Matchers.containsString("Silva"))
			.body("age", Matchers.greaterThan(18));	
	}
	
	@Test
	public void testeUsuarioOutrasFormas() {
		//https://restapi.wcaquino.me/users/1
		Response response = RestAssured.request(Method.GET,"https://restapi.wcaquino.me/users/1");
		
		System.out.println(response.body().asString());
		System.out.println(response.path("id").toString());
		System.out.println(response.path("name").toString());
		System.out.println(response.path("age").toString());
		
		//path
		Integer id = 1;
		Assert.assertEquals(id, response.path("id"));
		
		//jsonPath
		JsonPath jpPath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpPath.getInt("id"));
		
		//from
		int id2 = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id2);
		
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		//https://restapi.wcaquino.me/users/2
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(2))
			.body("name",Matchers.containsString("Joaquina"))
			.body("endereco.rua",Matchers.is("Rua dos bobos"));
	}
	
	@Test
	public void deveVerificarUmaLista() {
	//https://restapi.wcaquino.me/users/3
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name",Matchers.containsString("Ana"))
			.body("filhos",Matchers.hasSize(2))
			.body("filhos[0].name", Matchers.is("Zezinho"))
			.body("filhos[1].name", Matchers.is("Luizinho"))
			.body("filhos.name", Matchers.hasItem("Zezinho"))
			.body("filhos.name", Matchers.hasItems("Zezinho","Luizinho"))
		;
	
	}
	
	@Test
	public void deveRetornarErroUsuarioInexistente() {
	//https://restapi.wcaquino.me/users/4
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", Matchers.is("Usuário inexistente"))		
		;	
	}
	
	@Test
	public void deveVerificarListaNaRaiz() {
	//https://restapi.wcaquino.me/users
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", Matchers.hasSize(3))	//lista na raiz($) 3 itens	
			.body("", Matchers.hasSize(3))	//lista na raiz("") 3 itens	
			.body("name", Matchers.hasItems("João da Silva","Maria Joaquina","Ana Júlia"))
			.body("age[1]", Matchers.is(25))
			.body("filhos.name", Matchers.hasItems(Arrays.asList("Zezinho","Luizinho")))//dentro da lista,verifica se tem os itens
			.body("salary", Matchers.contains(1234.5677f,2500,null))//dentro da lista,verifica se tem os itens
		;	
	}
	
	@Test
	public void deveFazerVerificacoesAvancadas() {
	//https://restapi.wcaquino.me/users	
	//find e findAll -> são metodos do "Groovy", não do Java
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", Matchers.hasSize(3))	//lista na raiz($) 3 itens	
			.body("age.findAll{it <= 25 }.size()", Matchers.is(2))//retorna a quantidade de itens com age <= 25
			.body("age.findAll{it <= 25 && it > 20}.size()", Matchers.is(1))//retorna a quantidade de itens com age <= 25 e > 20
			.body("findAll{it.age <= 25 && it.age > 20}.name", Matchers.hasItem("Maria Joaquina"))//retona uma lista,verifica pelo filtro idade se tem esse nome
			.body("findAll{it.age <= 25}[0].name", Matchers.is("Maria Joaquina"))//pega apenas o objeto de posicao 0 da lista com idade <= 25, verifica se tem esse nome
			.body("findAll{it.age <= 25}[-1].name", Matchers.is("Ana Júlia"))//pega apenas o ultimo objeto da lista com idade <= 25, verifica se tem esse nome
			.body("findAll{it.age <= 25}[-2].name", Matchers.is("Maria Joaquina"))//pega apenas o penultimo objeto da lista com idade <= 25, verifica se tem esse nome
			.body("find{it.age <= 25}.name", Matchers.is("Maria Joaquina"))//find pega somente o primeiro objeto encontrado
			.body("findAll{it.name.contains('n')}.name", Matchers.hasItems("Maria Joaquina","Ana Júlia"))//verifica se na propriedade name contem a letra 'n'. verifica os nome
			.body("findAll{it.name.length() > 10}.name", Matchers.hasItems("João da Silva","Maria Joaquina"))//verifica itens na propriedade contem caracteres > 10. verifica os nome
			.body("name.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))//transforma um item para UpperCase. verifica os nome
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))//transforma um item para UpperCase. verifica os nome
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", Matchers.anyOf(Matchers.arrayContaining("MARIA JOAQUINA"),Matchers.arrayWithSize(1)))//transforma uma lista para UpperCase. verifica os nome
			.body("age.collect{it * 2}",Matchers.hasItems(60,50,40))//retorna as idades,pega as idades e multiplica por 2, verifica as idades
			.body("id.max()",Matchers.is(3))//retorna o id maximo da lista, verifica se é 3
			.body("salary.min()",Matchers.is(1234.5678f))//retorna o salario minimo da lista, verifica se é 1234.5678
			//.body("salary.findAll{it != null}.sum()",Matchers.is(3734.5678f))//retorna o salario minimo da lista, verifica se é 1234.5678
			.body("salary.findAll{it != null}.sum()",Matchers.is(Matchers.closeTo(3734.5678f,0.001)))//arredonda 0.1 e considera como certo
			.body("salary.findAll{it != null}.sum()",Matchers.anyOf(Matchers.greaterThan(3700d),Matchers.lessThan(3800d)))//verifica se a soma do salario esta entre esse intervalo
			;	
	}
	
	@Test
	public void deveUnirJsonPathComJava() {
		//cria um arraylist de nomes, passando como parametro o resultado da consulta, e depois faz a verificacao com Assert
		ArrayList<String> nomes =
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/users")
		.then()
			.extract().path("name.findAll{it.startsWith('Maria')}")
		;		
		Assert.assertEquals(1, nomes.size());
		Assert.assertTrue(nomes.get(0).equalsIgnoreCase("MAria JoaqUina"));
		Assert.assertEquals(nomes.get(0).toUpperCase(), "maria joaquina".toUpperCase());
	}
	
	@Test
	public void deveUnirJsonPathComJaXpath() {
		RestAssured
		.given()			
		.when()
			.get("https://restapi.wcaquino.me/usersXML")
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
}
