package platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class CodeSharingPlatform {
    Response response;
    Map<Integer, Response> savedRequests = new HashMap<>();
    Logger logger = LoggerFactory.getLogger(CodeSharingPlatform.class);
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) {
        SpringApplication.run(CodeSharingPlatform.class, args);
    }

    @GetMapping({"/{endPoint}/{action}"})
    @ResponseBody
    public ResponseEntity<String> httpHandler(@PathVariable String endPoint, @PathVariable String action, @RequestBody(required = false) String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "text/html");
        response = new Response();
        logger.info("endPoint=" + endPoint);
        if ("code".equals(endPoint)) {
            if ("new".equals(action)) {
            } else if ("latest".equals(action)) {
                if (!savedRequests.isEmpty()) {
                    List<DivBlockBuilder> divBlockBuilderList = new ArrayList<>();
                    int maxMapKey = savedRequests.keySet().stream().max(Integer::compareTo).orElse(0);
                    for (int i = 0; i < 10 && maxMapKey > 0; i++, maxMapKey--) {
                        DivBlockBuilder divBlock = new DivBlockBuilder();
                        Response response = savedRequests.get(maxMapKey);
                        divBlock.addTag(response.getDate().format(format), "span", "load_date", null);
                        divBlock.addTag(response.getCode(), "pre", "code_snippet", null);
                        divBlockBuilderList.add(divBlock);
                    }
                    HtmlBuilder latestResponse = new HtmlBuilder();
                    latestResponse.setTitle("Latest");
                    latestResponse.setDivBlocks(divBlockBuilderList);
                    return ResponseEntity.ok()
                            .headers(httpHeaders)
                            .body(latestResponse.getHtml());
                } else {
                    response.setCode("no saved code");
                }
            } else {
                int id = Integer.parseInt(action);
                if (id <= 0 || id > savedRequests.keySet().stream().max(Integer::compareTo).orElse(0)) {
                    response.setCode("");
                } else {
                    response = savedRequests.get(id);
                }
            }
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(response.getHtml());
        } else {
            return ResponseEntity.badRequest()
                    .headers(httpHeaders).build();
        }
    }

    @GetMapping(value = {"api/{endPoint}/{action}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apiGetHandler(@PathVariable String endPoint, @PathVariable String action) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        response = new Response();
        if ("code".equals(endPoint)) {
            if ("latest".equals(action)) {
                if (!savedRequests.isEmpty()) {
                    response = savedRequests.get(savedRequests.keySet().stream().max(Integer::compareTo).orElse(0));
                } else {
                    response.setCode("no saved code");
                }
            } else {
                int id = Integer.parseInt(action);
                if (id <= 0 || id > savedRequests.keySet().stream().max(Integer::compareTo).orElse(0)) {
                    return ResponseEntity.notFound()
                            .headers(httpHeaders)
                            .build();
                } else {
                    response = savedRequests.get(id);
                }
            }
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(response.getJson());
        } else {
            return ResponseEntity.badRequest()
                    .headers(httpHeaders).build();
        }
    }

    @PostMapping(value = {"api/{endPoint}/{action}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apiPostHandler(@PathVariable String endPoint, @PathVariable String action, @RequestBody Response response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        int id = savedRequests.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        savedRequests.put(id, response);
        if ("code".equals(endPoint) && "new".equals(action)) {
            this.response = response;
            this.response.setDate(LocalDateTime.now());
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body("{\"id\":\"" + id + "\"}");
        } else {
            return null;
        }
    }
}
