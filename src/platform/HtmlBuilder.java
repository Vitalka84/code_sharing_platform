package platform;

import java.util.ArrayList;
import java.util.List;

public class HtmlBuilder {
    String title;
    List<String> bodyContent;
    List<String> jsFunctions;
    List<DivBlockBuilder> divBlocks;

    public HtmlBuilder() {
        this.bodyContent = new ArrayList<>();
        this.jsFunctions = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDivBlocks(List<DivBlockBuilder> divBlocks) {
        this.divBlocks = divBlocks;
    }

    public void setPreWrapper(String data, String id) {
        if (data != null && !"".equals(data)) {
            StringBuilder spanWrapper = new StringBuilder();
            spanWrapper.append("<pre");
            if (id != null) {
                spanWrapper.append(" id=\"" + id + "\"");
            }
            spanWrapper.append(">" + data + "</pre>");
            this.bodyContent.add(spanWrapper.toString());
        }
    }

    public void setSpanWrapper(String data, String id) {
        if (data != null && !"".equals(data)) {
            StringBuilder spanWrapper = new StringBuilder();
            spanWrapper.append("<span");
            if (id != null) {
                spanWrapper.append(" id=\"" + id + "\"");
            }
            spanWrapper.append(">" + data + "</span><br>");
            this.bodyContent.add(spanWrapper.toString());
        }
    }

    public void setTextAreaWrapper(String data) {
        if (data != null && !"".equals(data)) {
            this.bodyContent.add("<textarea id=\"code_snippet\">" + data + "</textarea><br>");
            this.bodyContent.add("<button id=\"send_snippet\" type=\"submit\" onclick=\"send()\">Submit</button>");
            this.jsFunctions.add(getSendJsFunction());
        }
    }

    public String getHtml() {
        StringBuffer res = new StringBuffer();
        res.append("<html>");
        res.append("<head>");
        res.append("<title>");
        res.append(this.title == null ? "Code" : this.title);
        res.append("</title>");
        res.append("<body>");
        if (divBlocks != null) {
            for (DivBlockBuilder divBlock : divBlocks) {
                res.append(divBlock.toString());
            }
        }
        for (String body : bodyContent) {
            res.append(body);
        }
        res.append("</body>");
        res.append("</head>");
        res.append("</html>");
        if (!jsFunctions.isEmpty()) {
            res.append("<script type=\"text/javascript\">");
            for (String function : jsFunctions) {
                res.append(function);
            }
            res.append("</script>");
        }
        return res.toString();
    }

    private String getSendJsFunction() {
        return "function send() {\n" +
                "    let object = {\n" +
                "        \"code\": document.getElementById(\"code_snippet\").value\n" +
                "    };\n" +
                "    let json = JSON.stringify(object);\n" +
                "    let xhr = new XMLHttpRequest();\n" +
                "    xhr.open(\"POST\", '/api/code/new', false)\n" +
                "    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');\n" +
                "    xhr.send(json);\n" +
                "    if (xhr.status == 200) {\n" +
                "      alert(\"Success!\");\n" +
                "    }\n" +
                "}";
    }

    public void setJsFunction(String jsFunction) {
        jsFunctions.add(jsFunction);
    }
}
