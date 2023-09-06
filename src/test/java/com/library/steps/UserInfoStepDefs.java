package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class UserInfoStepDefs {
    String token;
    RequestSpecification request;
    Response response;
    String pathId;


    @Given("I logged Library api as a {string}")
    public void ı_logged_library_api_as_a(String role) {
        String email = ConfigurationReader.getProperty("librarian_username");
        String password = ConfigurationReader.getProperty("librarian_password");
        token = LibraryAPI_Util.getToken(email, password);

        //LibraryAPI_Util.getToken(role);
    }

    @Given("Accept header is {string}")
    public void accept_header_is(String contentType) {

        request = given().header("x-library-token", token)
                .and().accept(ContentType.JSON);
    }

    @When("I send GET request to {string} endpoint")
    public void ı_send_get_request_to_endpoint(String endPoint) {
        response = request.when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint);

    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        assertThat(response.contentType(), is(contentType));
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String field) {
        assertThat(response.path(field), everyItem(is(notNullValue())));
    }

    @Given("Path param is {string}")
    public void path_param_is(String id) {
        pathId = id;
        request.pathParam("id", pathId);

    }

    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String idValue) {
        assertThat(response.path(idValue), is(pathId));
    }

    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fields) {
        for (String field : fields) {
            assertThat(response.path(field),is(notNullValue()));
        }
    }
}
