package platform;

import java.util.ArrayList;
import java.util.List;

public class HtmlBuilder {
    String title;
    List<String> bodyContent;

    public HtmlBuilder() {
        bodyContent = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCodeBlock(String codeBlock) {
        if (codeBlock != null && !"".equals(codeBlock)) {
            this.bodyContent.add(codeBlock);
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
        for (String sharedCode : bodyContent) {
            res.append("<pre>");
            res.append(sharedCode);
            res.append("</pre>");
        }
        res.append("</body>");
        res.append("</head>");
        res.append("</html>");
        return res.toString();
    }
}
