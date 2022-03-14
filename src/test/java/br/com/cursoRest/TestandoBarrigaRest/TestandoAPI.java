package br.com.cursoRest.TestandoBarrigaRest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;

public class TestandoAPI {

	@Test
	public void deveFazerAutenticacaoComToken() {
		//Login na API	//Receber Token //Obter as contas
		
		Map<String, String> login = new HashMap<>();
		login.put("email", "joao_marcossilva@hotmail.com");
		login.put("senha", "123456");

		given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("https://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
		;
	}
	
	@Test
	public void deveFazerAutenticacaoComToken2() {
		Map<String, String> login = new HashMap<>();
		login.put("email", "joao_marcossilva@hotmail.com");
		login.put("senha", "123456");

		//Login na API	//Receber Token
		String token = given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("https://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
		;
		
		//Obter as contas		
			given()
				.log().all()
				.header("Authorization","JWT "+token)
			.when()
				.get("https://barrigarest.wcaquino.me/contas")
			.then()
				.log().all()
				.statusCode(200)
				.body("nome", hasItem("conta de teste"))
			;
	}
	
	@Test
	public void deveFazerLoginAplicacaoWeb() {
		//login
		String cookie = given()
			.log().all()
			.formParam("email", "joao_marcossilva@hotmail.com")
			.formParam("senha", "123456")
			.contentType(ContentType.URLENC.withCharset("UTF-8"))
		.when()
			.post("http://seubarriga.wcaquino.me/logar")
		.then()
			.log().all()
			.statusCode(200)
			.extract().header("set-cookie");
		;
		
		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println(cookie);
		
		
		  String dados = given()
				.log().all()
				.cookies("connect.sid",cookie)
			.when()
				.get("http://seubarriga.wcaquino.me/contas")
			.then()
				.log().all()
				.statusCode(200)
				.body("html.body.table.tbody.tr[0].td[0]", is("conta de teste"))
				.extract().body().asString()
			;
		  System.out.println("---------------");
		  XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, dados);
		  System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
	}
}
