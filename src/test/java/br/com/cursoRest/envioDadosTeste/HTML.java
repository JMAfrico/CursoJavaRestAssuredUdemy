package br.com.cursoRest.envioDadosTeste;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;
import org.junit.Test;
import io.restassured.http.ContentType;


public class HTML {

	@Test
	public void deveFazerBuscasComHTML() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
		;	
	}
	
	@Test
	public void deveFazerBuscasComHTML2() {
		given()
			.log().all()
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.appendRootPath("html.body.div.table.tbody")
			.body("tr.size()", is(3))
			.body("tr[1].td[2]", is("25"))
			.body("tr[2].td[1]", is("Ana Júlia"))
			.body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
		;	
	}
	
	@Test
	public void deveFazerBuscasComHTMLXpath() {
		given()
			.log().all()
			.queryParam("format","clean")
		.when()
			.get("https://restapi.wcaquino.me/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.appendRootPath("html.body.div.table.tbody")
			.body(hasXPath("count(//tr)", is("4")))
			.body(hasXPath("//td[text()=2]/../td[2]", is("Maria Joaquina")));
		;	
	}
}
