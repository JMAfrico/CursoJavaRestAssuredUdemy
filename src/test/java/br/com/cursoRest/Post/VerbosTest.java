package br.com.cursoRest.Post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTest {


	@Test
	public void deveSalvarUsuarioJson() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\":\"José\",\"age\":30}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("José"))
			.body("age", is(30))
		;
	}
	
	@Test
	public void deveSalvarUsuarioXML() {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Jose</name><age>30</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Jose"))
			.body("user.age", is("30"))
		;
	}		
	
	@Test
	public void naoDeveSalvarUsuarioSemNomeJson() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"age\":30}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", containsString("Name é um atributo obrigatório"))
		;
	}
	
	@Test
	public void naoDeveSalvarUsuarioSemNomeXML() {
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><age>30</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(400)
			.body("user.@id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
		;
	}
	
	@Test
	public void deveAlterarUsuarioJson() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\":\"Usuario alterado\",\"age\":80}")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
		;
	}
	
//	@Test
//	public void deveAlterarUsuarioXML() {
//		given()
//			.log().all()
//			.contentType(ContentType.XML)
//			.body("<user><name>Jose</name><age>30</age></user>")
//		.when()
//			.put("https://restapi.wcaquino.me/usersXML/1")
//		.then()
//			.log().all()
//			.statusCode(200)
//			.body("user.@id", is(1))
//			.body("user.name", is("Usuario alterado"))
//			.body("user.age", is(80))
//		;
//	}
	
	@Test
	public void deveCustomizarURL() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\":\"Usuario alterado\",\"age\":80}")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}","users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
		;
	}
	
	@Test
	public void deveCustomizarURL2() {
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{\"name\":\"Usuario alterado\",\"age\":80}")
			.pathParam("entidade","users")
			.pathParam("userId","1")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuario alterado"))
			.body("age", is(80))
		;
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void naoDeveRemoverUsuarioInexistente() {
		given()
			.log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/6")
		.then()
			.log().all()
			.statusCode(400)
			.body("error",is("Registro inexistente"))
		;
	}

}
