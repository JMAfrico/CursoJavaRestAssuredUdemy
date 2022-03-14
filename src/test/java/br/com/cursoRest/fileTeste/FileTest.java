package br.com.cursoRest.fileTeste;

import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class FileTest {

	@Test
	public void deveFazerUploadArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/test/resources/PRATICASGIT.pdf"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name",is("PRATICASGIT.pdf"))
		;
	}
	
	@Test
	public void deveMostrarMensagemErroArquivoNaoEnviado() {
		given()
			.log().all()
		.when()
			.post("https://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404)
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void naoDeveFazerUploadArquivoGrande() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/test/resources/vs_community.exe"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(413)
			.body(containsString("Request Entity Too Large"))
		;
	}
	
	@Test
	public void naoDeveFazerUploadArquivoGrandeComTimeLimite() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/test/resources/vs_community.exe"))
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(413)
			.time(lessThan(5000L))
			.body(containsString("Request Entity Too Large"))
		;
	}
	
	@Test
	public void deveFazerDownloadArquivo() throws IOException {
		byte[] Byteimagem = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
			//.log().all()
			.statusCode(200)
			.extract().asByteArray();
		;
		File imagem = new File("src/test/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(Byteimagem);
		out.close();
		
		Assert.assertThat(imagem.length(), lessThan(100000L));
	}

}
