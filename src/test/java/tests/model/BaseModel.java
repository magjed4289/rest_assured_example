package tests.model;

import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import tests.utils.ConfigFileReader;

@Data
public class BaseModel {
    private Response response;
    private Header header;
    private ConfigFileReader config = ConfigFileReader.getInstance();
    private RequestSpecification requestSpecification;

    public BaseModel() {
    }
}
