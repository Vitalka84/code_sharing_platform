package platform;

public class Response {
    String code;

    public Response(String code) {
        this.code = code;
    }

    public String getHtml() {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        htmlBuilder.setCodeBlock(code);
        return htmlBuilder.getHtml();
    }

    public String getJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"code\":\"").append(code).append("\"");
        builder.append("}");
        return builder.toString();
    }
}
