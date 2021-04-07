package platform;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@SpringBootApplication
@RestController
public class CodeSharingPlatform {

    public static void main(String[] args) {
        SpringApplication.run(CodeSharingPlatform.class, args);
    }

    //    @GetMapping("/{endPoint}")
    @GetMapping({"/", "/{endPoint}"})
    public ResponseEntity<String> httpHandler(@PathVariable(value = "endPoint", required = false)
                                                      Optional<String> endPoint) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "text/html");
        switch (endPoint.isPresent() ? endPoint.get() : "error") {
            case "code":
                return ResponseEntity.ok()
                        .headers(httpHeaders)
                        .body(new Response("public static void main(String[] args) {\n    SpringApplication.run(CodeSharingPlatform.class, args);\n}").getHtml());
            default:
                return null;

        }
    }

    @GetMapping(value = {"api/", "api/{endPoint}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apiHandler(@PathVariable(value = "endPoint", required = false)
                                                     Optional<String> endPoint) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        switch (endPoint.isPresent() ? endPoint.get() : "error") {
            case "code":
                return ResponseEntity.ok()
                        .headers(httpHeaders)
                        .body(new Response("public static void main(String[] args) {\n    SpringApplication.run(CodeSharingPlatform.class, args);\n}").getJson());
            default:
                return null;

        }
    }
}
