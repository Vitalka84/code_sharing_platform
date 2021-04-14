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
    Response[] latest;

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
                    latest = getLatest(10);
                    for (int i = 0; i < latest.length; i++) {
                        DivBlockBuilder divBlock = new DivBlockBuilder();
                        divBlock.addTag(latest[i].getDate().format(format), "span", "load_date", null);
                        divBlock.addTag(latest[i].getCode(), "pre", "code_snippet", null);
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
                    latest = getLatest(10);
                    StringBuilder jsonRespons = new StringBuilder();
                    jsonRespons.append("[");
                    for (int i = 0; i < latest.length; i++) {
                        jsonRespons.append(latest[i].getJson());
                        if (i < latest.length - 1) {
                            jsonRespons.append(",");
                        }
                    }
                    jsonRespons.append("]");
                    return ResponseEntity.ok()
                            .headers(httpHeaders)
                            .body(jsonRespons.toString());
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

    private Response[] getLatest(int count) {
        int countSavedRequests = savedRequests.keySet().stream().max(Integer::compareTo).orElse(0);
        int arrayLength = countSavedRequests > count ? count : countSavedRequests;
        Response[] res = new Response[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            res[i] = savedRequests.get(countSavedRequests - i);
        }
        return res;
    }
}
