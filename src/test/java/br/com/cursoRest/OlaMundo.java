package br.com.cursoRest;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;


public class OlaMundo {

	public static void main(String[] args) {
		//http://restapi.wcaquino.me/ola
		//http://localhost:80/api
		//http://127.0.0.1/api
		Response response = RestAssured.request(Method.GET,"http://restapi.wcaquino.me/ola");
		System.out.println(response.getBody().asString());
		System.out.println(response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
	}
}
