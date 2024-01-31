package com.diskfood.diskfoodapi.api;

import com.diskfood.diskfoodapi.domain.model.Cozinha;
import com.diskfood.diskfoodapi.domain.repository.CozinhaRepository;
import com.diskfood.diskfoodapi.util.DatabaseCleaner;
import com.diskfood.diskfoodapi.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaIntegrationIT {

     @LocalServerPort
     private int port;

     @Autowired
     private DatabaseCleaner databaseCleaner;

     @Autowired
     private CozinhaRepository cozinhaRepository;

     private int countCozinha;
     private Cozinha cozinhaAmericana;

     private String jsonCorretoCozinhaChinesa;



    @Before
     public void setUp(){
          RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
          RestAssured.port = port;
          RestAssured.basePath = "/cozinhas";

          jsonCorretoCozinhaChinesa = ResourceUtils.getContentFromResource(
                "/json/correto/cozinha-chinesa.json");

          databaseCleaner.clearTables();
          prepararDados();
     }

     @Test
     public void deveRetornarStatus200QuandoConsultarCozinhas(){
          given().accept(ContentType.JSON)
             .when()
                  .get()
             .then()
                  .statusCode(HttpStatus.OK.value());
     }

     @Test
     public void deveConter2CozinhasQuandoConsultarCozinhas(){
          given().accept(ContentType.JSON)
                  .when()
                  .get()
                  .then()
                  .body("nome", Matchers.hasSize(countCozinha));
     }

     @Test
     public void deveRetornarStatus201QuandoCadastrarCozinha(){
         given()
                 .body("{\"nome\": \"Chinesa\"}")
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                 .when()
                    .post()
                 .then()
                    .statusCode(HttpStatus.CREATED.value());
     }

     //get /cozinha/{id}
    @Test
    public void deveRetornarRespostaEStatusCorretosQuandoConsultarCozinhaExistente(){
        given()
                .pathParam("cozinhaId", 2)
                .accept(ContentType.JSON)
            .when()
                .get("/{cozinhaId}")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("Americana"));
    }


    @Test
    public void deveRetornarStatus404_QuandoConsultarCozinhaInexistente(){
        given()
                .pathParam("cozinhaId", 200)
                .accept(ContentType.JSON)
                .when()
                .get("/{cozinhaId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

     private void prepararDados(){
         Cozinha cozinha1 = new Cozinha();
         cozinha1.setNome("Tailandesa");
         cozinhaRepository.save(cozinha1);

         cozinhaAmericana = new Cozinha();
         cozinhaAmericana.setNome("Americana");
         cozinhaRepository.save(cozinhaAmericana);
         countCozinha = (int) cozinhaRepository.count();

     }

}
