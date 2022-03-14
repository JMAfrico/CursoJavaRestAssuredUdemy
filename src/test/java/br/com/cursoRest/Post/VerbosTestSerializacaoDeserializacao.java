package br.com.cursoRest.Post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;


public class VerbosTestSerializacaoDeserializacao {

	//-json
	
	@Test
	public void deveSalvarUsuarioSerializarJsonUsandoMap() {
		//IMPORTAR DEPENDENCIA GSON = <!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
		//MAP PARA JSON
		Map<String, Object> params = new HashMap<>();
		params.put("name","José");
		params.put("age", 30);
			
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(params)
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
	public void deveSalvarUsuarioSerializarJsonUsandoObjeto() {
		//OBJETO PARA JSON
		
		User user = new User("José",30);
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(user)
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
	public void deveSalvarUsuarioDeserializarJsonUsandoObjeto() {	
		User user = new User("Usuario deserializado",30);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;			
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getNome());
		Assert.assertThat(usuarioInserido.getAge(),is(30));
	}
	
	//xml
	
	@Test
	public void deveSalvarUsuarioSerializandoXMLUsandoObjeto() {	
		User user = new User("Usuario deserializadoXML",30);
		
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Usuario deserializadoXML"))
			.body("user.age", is("30"))
		;		
	}
	
	@Test
	public void deveSalvarUsuarioDeserializarXMLUsandoObjeto() {	
		User user = new User("Usuario deserializadoXML",30);
		
		User usuarioInserido = 
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		
		Assert.assertThat(usuarioInserido.getId(), is(notNullValue()));
		Assert.assertThat(usuarioInserido.getNome(), is("Usuario deserializadoXML"));
		Assert.assertThat(usuarioInserido.getAge(), is(30));
		Assert.assertThat(usuarioInserido.getSalary(), is(nullValue()));
	}	
}
