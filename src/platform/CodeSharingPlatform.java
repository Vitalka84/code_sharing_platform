package platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
@RestController
public class CodeSharingPlatform {
    Response response;

    public static void main(String[] args) {
        SpringApplication.run(CodeSharingPlatform.class, args);
    }

    @GetMapping({"/", "/{endPoint}", "/{endPoint}/{action}"})
    @ResponseBody
    public ResponseEntity<String> httpHandler(@PathVariable(value = "endPoint", required = false)
                                                      Optional<String> endPoint, @PathVariable(value = "action", required = false)
                                                      Optional<String> action, @RequestBody(required = false) String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "text/html");
        if (endPoint.isPresent() && "code".equals(endPoint.get())) {
            if (action.isPresent() && "new".equals(action.get())) {
                response = new Response();
            } else if (response == null) {
                response = new Response();
                response.setCode("public static void main(String[] args) {\n" +
                        "        SpringApplication.run(CodeSharingPlatform.class, args);\n" +
                        "    }");
                response.setDate(LocalDateTime.now());
            }
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(response.getHtml());
        } else {
            return null;
        }
    }

    @GetMapping(value = {"api/", "api/{endPoint}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apiGetHandler(@PathVariable(value = "endPoint", required = false)
                                                        Optional<String> endPoint) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        if (endPoint.isPresent() && "code".equals(endPoint.get())) {
            if (response == null) {
                response = new Response();
                response.setCode("public static void main(String[] args) {\n" +
                        "        SpringApplication.run(CodeSharingPlatform.class, args);\n" +
                        "    }");
                response.setDate(LocalDateTime.now());
            }
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(response.getJson());
        } else {
            return null;
        }
    }

    @PostMapping(value = {"api/", "api/{endPoint}", "api/{endPoint}/{action}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apiPostHandler(@PathVariable(value = "endPoint", required = false)
                                                         Optional<String> endPoint, @PathVariable(value = "action", required = false)
                                                         Optional<String> action, @RequestBody(required = false) Response response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        if (endPoint.isPresent() && "code".equals(endPoint.get()) && action.isPresent() && "new".equals(action.get())) {
            this.response = response;
            this.response.setDate(LocalDateTime.now());
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body("{}");
        } else {
            return null;
        }
    }
}
