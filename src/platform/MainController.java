package platform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import platform.models.SharedCode;
import platform.storage.SharedCodeDAO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    private SharedCodeDAO sharedCodeDAO;

    Response response;
    Logger logger = LoggerFactory.getLogger(MainController.class);
    List<SharedCode> latestN;
    SharedCode sharedCode;

    @GetMapping({"/{endPoint}/{action}"})
    @ResponseBody
    public ResponseEntity<String> httpHandler(@PathVariable String endPoint, @PathVariable String action, @RequestBody(required = false) String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "text/html");
        HtmlBuilder htmlResponse = new HtmlBuilder();
        htmlResponse.setLink("//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css", "stylesheet", null);
        htmlResponse.setScriptTag(null, "//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js");
        htmlResponse.setScriptTag("hljs.initHighlightingOnLoad();", null);
        List<DivBlockBuilder> divBlockBuilderList = new ArrayList<>();
        response = new Response();
        logger.info("endPoint=" + endPoint);
        if ("code".equals(endPoint)) {
            if ("new".equals(action)) {
                return ResponseEntity.ok()
                        .headers(httpHeaders)
                        .body(response.getHtml());
            } else if ("latest".equals(action)) {
                htmlResponse.setTitle("Latest");
                latestN = sharedCodeDAO.findTop10ByOrderByRecordIdDesc();
                if (!latestN.isEmpty()) {
                    for (SharedCode sharedCode : latestN) {
                        divBlockBuilderList.add(sharedCode.getDivBlock());
                    }
                } else {
                    return ResponseEntity.notFound()
                            .headers(httpHeaders)
                            .build();
                }
            } else {
                int id = Integer.parseInt(action);
                SharedCode requestedCode = sharedCodeDAO.findSharedCodeByRecordId(id);
                if (requestedCode == null) {
                    return ResponseEntity.notFound()
                            .headers(httpHeaders)
                            .build();
                } else {
                    divBlockBuilderList.add(requestedCode.getDivBlock());
                }
            }
            htmlResponse.setDivBlocks(divBlockBuilderList);
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(htmlResponse.getHtml());

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
                SharedCode[] latestA = sharedCodeDAO.findTop10ByOrderByRecordIdDesc().stream().toArray(SharedCode[]::new);
                if (latestA.length > 0) {
                    StringBuilder jsonRespons = new StringBuilder();
                    jsonRespons.append("[");
                    for (int i = 0; i < latestA.length; i++) {
                        jsonRespons.append(latestA[i].getJson());
                        if (i < latestA.length - 1) {
                            jsonRespons.append(",");
                        }
                    }
                    jsonRespons.append("]");
                    return ResponseEntity.ok()
                            .headers(httpHeaders)
                            .body(jsonRespons.toString());
                } else {
                    response.setCode("no saved code");
                    return ResponseEntity.notFound()
                            .headers(httpHeaders)
                            .build();
                }
            } else {
                int id = Integer.parseInt(action);
                SharedCode requestedCode = sharedCodeDAO.findSharedCodeByRecordId(id);
                if (requestedCode == null) {
                    return ResponseEntity.notFound()
                            .headers(httpHeaders)
                            .build();
                } else {
                    return ResponseEntity.ok()
                            .headers(httpHeaders)
                            .body(requestedCode.getJson());
                }
            }
        }
        return ResponseEntity.badRequest()
                .headers(httpHeaders).build();
    }

    @PostMapping(value = {"api/{endPoint}/{action}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> apiPostHandler(@PathVariable String endPoint, @PathVariable String action, @RequestBody String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");
        sharedCode = new SharedCode();
        sharedCode.setSharedCode(code);
        sharedCode.setDate(LocalDateTime.now());
        sharedCodeDAO.save(sharedCode);
        if ("code".equals(endPoint) && "new".equals(action)) {
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body("{\"id\":\"" + sharedCode.getRecordId() + "\"}");
        } else {
            return null;
        }
    }
}
