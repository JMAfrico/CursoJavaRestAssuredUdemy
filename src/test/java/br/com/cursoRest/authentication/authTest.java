package br.com.cursoRest.authentication;

import static org.hamcrest.Matchers.*;
import org.junit.Test;

import static io.restassured.RestAssured.*;
public class authTest {

	@Test
	public void deveAcessarSWAPI() {		
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
			
		;
	}
		
	//79ce62fe97ded7094a86a48b59f66c4a
	//api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}
	//https://api.openweathermap.org/data/2.5/weather?q=São%20Paulo&appid=79ce62fe97ded7094a86a48b59f66c4a&units=metric
	
	@Test
	public void deveAcessarOpenWeatherMapObterClimaFortaleza2() {		
		given()
			.log().all()
			.queryParam("q","Fortaleza,BR")
			.queryParam("appid", "79ce62fe97ded7094a86a48b59f66c4a")
			.queryParam("units", "metric")
		.when()
			.get("https://api.openweathermap.org/data/2.5/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Fortaleza"))
			.body("id", is(6320062))
			.body("main.temp", greaterThan(25f))
		;
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {		
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
		;
	}
	
	@Test
	public void deveAcessarComAutenticacaoBasica() {		
		given()
			.log().all()
		.when()
			.get("https://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveAcessarComAutenticacaoBasica2() {		
		given()
			.log().all()
			.auth().basic("admin","senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveAcessarComAutenticacaoBasicaChallenger() {	
		//usa autenticacao Hash, código unidirecional que o usuario encripta a senha
		//e envia para o servidor, o servidor verifica se o código hash é o mesmo
		//validando assim que não houve interceptação na comunicação
		given()
			.log().all()
			.auth().preemptive().basic("admin","senha")
		.when()
			.get("https://restapi.wcaquino.me/basicauth2")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
}
