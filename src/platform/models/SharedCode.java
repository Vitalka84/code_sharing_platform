package platform.models;

import platform.DivBlockBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "shared_codes")
public class SharedCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "record_id", nullable = false)
    private int recordId;

    @Column(name = "date", columnDefinition = "DATETIME NOT NULL")
    private LocalDateTime date;

    @Column(name = "shared_code", columnDefinition = "CLOB")
    private String sharedCode;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getSharedCode() {
        return sharedCode;
    }

    public void setSharedCode(String sharedCode) {
        this.sharedCode = sharedCode;
    }

    public DivBlockBuilder getDivBlock() {
        DateTimeFormatter format =DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DivBlockBuilder divBlock = new DivBlockBuilder();
        if (this.date != null) {
            divBlock.addTag(this.date.format(format), "span", "load_date", null);
        }
        if (this.sharedCode != null) {
            String codeTag = divBlock.genTag(this.sharedCode, "code", null, null);
            divBlock.addTag(codeTag, "pre", "code_snippet", null);
        }
        return divBlock;
    }

    public String getJson() {
        DateTimeFormatter format =DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if (sharedCode != null) {
            builder.append("\"code\":\"").append(sharedCode.replace("\n", "\\n")).append("\"");
        }
        if (date != null) {
            builder.append(",\"date\":\"").append(date.format(format)).append("\"");
        }
        builder.append("}");
        return builder.toString();
    }
}
