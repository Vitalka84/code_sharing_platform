package platform;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TestDrive {
    public static void main(String[] args) {
        long unixTime = System.currentTimeMillis() / 1000L;

        System.out.println(Timestamp.from(Instant.now()));
    }
}

