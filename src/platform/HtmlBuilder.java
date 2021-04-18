package platform;

import java.util.ArrayList;
import java.util.List;

public class HtmlBuilder {
    private String title;
    private List<String> bodyContent;
    private List<String> jsFunctions;
    private List<String> linksList;
    private List<String> scriptTagsList;
    private List<DivBlockBuilder> divBlocks;

    public HtmlBuilder() {
        this.bodyContent = new ArrayList<>();
        this.jsFunctions = new ArrayList<>();
        this.linksList = new ArrayList<>();
        this.scriptTagsList = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDivBlocks(List<DivBlockBuilder> divBlocks) {
        this.divBlocks = divBlocks;
    }

    public void setLink(String href, String rel, String type) {
        if (href != null && !"".equals(href)) {
            StringBuilder linkTag = new StringBuilder();
            linkTag.append("<link href=\"" + href + "\"");
            if (rel != null && !"".equals(rel)) {
                linkTag.append(" rel=\"" + rel + "\"");
            }
            if (type != null && !"".equals(type)) {
                linkTag.append(" rel=\"" + type + "\"");
            }
            linkTag.append(">");
            linksList.add(linkTag.toString());
        }
    }

    public void setScriptTag(String data, String src) {
        if ((data != null && !"".equals(data)) || (src != null && !"".equals(src))) {
            StringBuilder scriptTag = new StringBuilder();
            scriptTag.append("<script");
            if (src != null && !"".equals(src)) {
                scriptTag.append(" src=\"" + src + "\"");
            }
            scriptTag.append(">");
            if (data != null && !"".equals(data)) {
                scriptTag.append(data);
            }
            scriptTag.append("</script>");
            scriptTagsList.add(scriptTag.toString());
        }
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
        if (linksList != null) {
            for (String link : linksList) {
                res.append(link);
            }
        }
        if (scriptTagsList != null) {
            for (String scriptTag : scriptTagsList) {
                res.append(scriptTag);
            }
        }
        res.append("</head>");
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
