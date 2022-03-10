package br.com.cursoRest;

import static io.restassured.RestAssured.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testeOlaMundo() {
		Response response = request(Method.GET,"http://restapi.wcaquino.me/ola");
		Assert.assertEquals("Ola Mundo!",response.getBody().asString());
		Assert.assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void DevoConhecerOutrasFormasDeRestAssured() {
		/////--------MÉTODO 1--------------/////
		
		//Isso
		Response response = request(Method.GET,"http://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		//É o mesmo que isso
		RestAssured.get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		
		
		/////--------MÉTODO 2--------------/////
		//adicionando import static não precisa nomear 'RestAssured'
		get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		
		
		
		/////--------MÉTODO 3--------------/////
		//Given When Then
		RestAssured
		.given()
			//pré condicoes
		.when()
			//acao
			.get("http://restapi.wcaquino.me/ola")
		.then()
			//validacao
			.statusCode(200);
	}
	
	@Test
	public void devoConhecerMetchersHamcrest() {
		Assert.assertThat("Maria", Matchers.is("Maria"));//atual, esperado
		Assert.assertThat(128, Matchers.is(128));
		Assert.assertThat(128, Matchers.isA(Integer.class));//se 128 é um inteiro
		Assert.assertThat(128.00, Matchers.isA(Double.class));//se 128.00 é um double
		Assert.assertThat(201.00, Matchers.greaterThanOrEqualTo(200.00));//se 201 é maior ou igual a 200
		Assert.assertThat(2.00, Matchers.lessThan(200.00));//se 2 é menor ou igual a 200
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9,11);
		
		Assert.assertThat(impares, Matchers.hasSize(6));//verifica se a lista tem 6 elementos
		Assert.assertThat(impares, Matchers.contains(1,3,5,7,9,11));//verifica se a lista tem esses elementos
		Assert.assertThat(impares, Matchers.containsInAnyOrder(3,5,1,9,11,7));//verifica se a lista tem esses elementos em qualquer ordem
		Assert.assertThat(impares, Matchers.hasItem(1));//verifica se tem esse item
		Assert.assertThat(impares, Matchers.hasItems(1,5));//verifica se tem esses items
		
		Assert.assertThat("Maria", Matchers.is(Matchers.not("Davinha")));//Maria nao é Davinha
		Assert.assertThat("Maria", Matchers.not("Davinha"));//Maria nao é Davinha
		Assert.assertThat("Maria", Matchers.anyOf(Matchers.is("Maria"), Matchers.is("Joaquina")));//(OR) qualquer Maria é Maria ou Joaquina
		Assert.assertThat("Maria", Matchers.allOf(Matchers.startsWith("Ma"), Matchers.endsWith("ia"), Matchers.containsString("ri")));

	}
	
	@Test
	public void devoValidarBody() {
		
		RestAssured
		.given()
			//pré condicoes
		.when()
			//acao
			.get("http://restapi.wcaquino.me/ola")
		.then()
			//validacao
			.statusCode(200)
			.body(Matchers.is("Ola Mundo!"))
			.body(Matchers.containsString("Ola"))
			.body(Matchers.not(Matchers.nullValue()));
	}
}
