package platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Response {
    String code;
    LocalDateTime date;
    DateTimeFormatter format;

    public Response(String code) {
        this.code = code.replace("\n", "\\n");
        this.date = LocalDateTime.now();
    }

    public String getHtml() {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        if (date != null) {
            this.format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            htmlBuilder.setSpanWrapper(date.format(format), null);
        }
        if (code != null) {
            htmlBuilder.setTextAreaWrapper(code);
            htmlBuilder.setTitle("Code");
        } else {
            htmlBuilder.setTitle("Create");
        }
        return htmlBuilder.getHtml();
    }

    public Response() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"code\":\"").append(code).append("\"");
        builder.append(",\"date\":\"").append(date.format(format)).append("\"");
        builder.append("}");
        return builder.toString();
    }
}
