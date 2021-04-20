package platform;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TestDrive {
    public static void main(String[] args) {
        LocalDateTime fromDateTime = LocalDateTime.of(2021, 04, 20, 14, 39, 46);
        LocalDateTime toDateTime = LocalDateTime.now();
        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);
        long res = ChronoUnit.SECONDS.between(toDateTime, fromDateTime.plusSeconds(4000));

        System.out.println(res);
    }
}

