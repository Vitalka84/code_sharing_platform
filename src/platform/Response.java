package platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Response {
    String code;
    LocalDateTime date;
    DateTimeFormatter format;

    public Response(String code) {
        this.code = code.replace("\n", "\\n");
        this.format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.date = LocalDateTime.now();
    }

    public String getHtml() {
        HtmlBuilder htmlBuilder = new HtmlBuilder();
        if (date != null) {
            htmlBuilder.setSpanWrapper(date.format(format), "load_date");
        }
        if (code != null) {
            htmlBuilder.setPreWrapper(code, "code_snippet");
            htmlBuilder.setTitle("Code");
        } else {
            htmlBuilder.setTextAreaWrapper("...");
            htmlBuilder.setTitle("Create");
        }
        return htmlBuilder.getHtml();
    }

    public Response() {
        this.format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
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

    public DivBlockBuilder getDivBlock() {
        DivBlockBuilder divBlock = new DivBlockBuilder();
        if (this.date != null) {
            divBlock.addTag(this.date.format(format), "span", "load_date", null);
        }
        if (this.code != null) {
            String codeTag = divBlock.genTag(this.code, "code", null, null);
            divBlock.addTag(codeTag, "pre", "code_snippet", null);
        }
        return divBlock;
    }
}
