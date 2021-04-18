package platform;

import java.util.ArrayList;
import java.util.List;

public class DivBlockBuilder {
    private List<String> divContent;
    private String cssId;
    private String cssClass;
    private StringBuilder newTag;

    public DivBlockBuilder() {
        this.divContent = new ArrayList<>();
    }

    public void setCssId(String cssId) {
        this.cssId = cssId;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void addTag(String data, String wrapper, String id, String cssClass) {
        if (data != null && !"".equals(data)) {
            newTag = new StringBuilder();
            newTag.append("<" + wrapper);
            if (id != null) {
                newTag.append(" id=\"" + id + "\"");
            }
            if (cssClass != null) {
                newTag.append(" class=\"" + cssClass + "\"");
            }
            newTag.append(">" + data + "</" + wrapper + ">");
            this.divContent.add(newTag.toString());
        }
    }

    public String genTag(String data, String wrapper, String id, String cssClass) {
        newTag = new StringBuilder();
        if (data != null && !"".equals(data)) {
            newTag.append("<" + wrapper);
            if (id != null) {
                newTag.append(" id=\"" + id + "\"");
            }
            if (cssClass != null) {
                newTag.append(" class=\"" + cssClass + "\"");
            }
            newTag.append(">" + data + "</" + wrapper + ">");
        }
        return newTag.toString();
    }

    @Override
    public String toString() {
        StringBuilder divBlock = new StringBuilder();
        divBlock.append("<div");
        if (this.cssId != null) {
            divBlock.append(" id=\"" + this.cssId + "\"");
        }
        if (this.cssClass != null) {
            divBlock.append(" class=\"" + this.cssClass + "\"");
        }
        divBlock.append(">");
        for (String content : this.divContent) {
            divBlock.append(content);
        }
        divBlock.append("</div>");
        return divBlock.toString();
    }
}
