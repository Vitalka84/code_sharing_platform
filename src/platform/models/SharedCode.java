package platform.models;

import org.hibernate.annotations.ColumnDefault;
import platform.DivBlockBuilder;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.UUID;

@Entity
@Table(name = "shared_codes")
public class SharedCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "record_id", nullable = false)
    private int recordId;

    //    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "record_uuid", updatable = false, nullable = false)
//    @ColumnDefault("random_uuid()")
    private UUID recordUUID;

    @Column(name = "date_unix_time")
    private long dateUnixTime;

    @Column(name = "shared_code", columnDefinition = "CLOB")
    private String sharedCode;

    @Column(name = "views")
    @ColumnDefault("0")
    private int views;

    @Column(name = "allowed_views")
    private int allowedViews;

    @Column(name = "viewing_time")
    @ColumnDefault("0")
    private int viewingTime;

    public LocalDateTime getDate() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(dateUnixTime), TimeZone.getDefault().toZoneId());
    }

    public void setDateUnixTime(LocalDateTime date) {
        ZoneId zoneId = ZoneId.systemDefault();
        this.dateUnixTime = date.atZone(zoneId).toEpochSecond();
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getViewingTime() {
        return viewingTime;
    }

    public void setViewingTime(int viewingTime) {
        this.viewingTime = viewingTime;
    }

    public UUID getRecordUUID() {
        return recordUUID;
    }

    public void setRecordUUID(UUID recordUUID) {
        this.recordUUID = recordUUID;
    }

    public int getAllowedViews() {
        return allowedViews;
    }

    public void setAllowedViews(int allowedViews) {
        this.allowedViews = allowedViews;
    }

    public void addView() {
        ++this.views;
    }

    public long getRemainingSeconds() {
        if (viewingTime == 0) {
            return 0;
        }
        long remainingSeconds = dateUnixTime + viewingTime - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return remainingSeconds > 0 ? remainingSeconds : 0;
    }

    public DivBlockBuilder getDivBlock() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        DivBlockBuilder divBlock = new DivBlockBuilder();
        if (this.dateUnixTime > 0) {
            divBlock.addTag(getDate().format(format), "span", "load_date", null);
        }
        if (this.sharedCode != null) {
            String codeTag = divBlock.genTag(this.sharedCode, "code", null, null);
            divBlock.addTag(codeTag, "pre", "code_snippet", null);
        }
        return divBlock;
    }

    public String getJson() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if (sharedCode != null) {
            builder.append("\"code\":\"").append(sharedCode.replace("\n", "\\n")).append("\"");
        }
        if (dateUnixTime > 0) {
            builder.append(",\"date\":\"").append(getDate().format(format)).append("\"");
        }
        builder.append(",\"time\":").append(getRemainingSeconds());
        builder.append(",\"views\":").append(views);
        builder.append("}");
        return builder.toString();
    }
}
