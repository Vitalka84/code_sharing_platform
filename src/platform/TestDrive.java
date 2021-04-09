package platform;

import java.io.File;

public class TestDrive {
    public static void main(String[] args) {
        File file = new File("src/platform/www/index.html");

        System.out.printf(file.toString());
    }
}

