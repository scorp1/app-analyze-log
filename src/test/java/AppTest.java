import app.Main;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class AppTest {

    @Test
    public void aggregateLogsInterval_shouldBadAvailability() throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        double availability = 99.9;
        int timeout = 44;
        String code = "200";
        int currentTimeout = 23;
        String timeStr = "16:47:02";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.parse(timeStr, formatter);
        Date time = java.sql.Time.valueOf(localTime);
        Date currentTime = new Date(time.getTime());
        Date endTime = new Date(currentTime.getTime() + TimeUnit.MINUTES.toMillis(1));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            // Запись данных в файл
            writer.write("192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();
            writer.write("192.168.32.181 - - [14/06/2017:16:47:22 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();
            writer.write("192.168.32.181 - - [14/06/2017:16:47:23 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();
            writer.write("192.168.32.181 - - [14/06/2017:16:47:30 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();
            writer.write("192.168.32.181 - - [14/06/2017:16:47:40 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 500 2 44.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();

        }
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {

            StringBuilder result = Main.aggregateLogsInterval(currentTime, endTime, reader, availability, timeout, code, currentTimeout);
            String str = result.toString();
            assertEquals("16:47:40  16:48:02  83,3", str);
        }

        File tempFile1 = File.createTempFile("test1", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile1))) {

            writer.write("192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();
            writer.write("192.168.32.181 - - [14/06/2017:16:47:12 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();
            writer.write("192.168.32.181 - - [14/06/2017:16:47:22 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 44.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();
            writer.write("192.168.32.181 - - [14/06/2017:16:47:30 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 55.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();
            writer.write("192.168.32.181 - - [14/06/2017:16:47:40 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 66.510983 \"-\" \"@list-item-updater\" prio:0");
            writer.newLine();

        }
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile1))) {

            StringBuilder result = Main.aggregateLogsInterval(currentTime, endTime, reader, availability, timeout, code, currentTimeout);
            String str = result.toString();
            assertEquals("16:47:40  16:48:02  66,7", str);
        }

        File tempFile2 = File.createTempFile("test2", ".txt");
        try (BufferedWriter writer2 = new BufferedWriter(new FileWriter(tempFile2))) {

            writer2.write("192.168.32.181 - - [14/06/2017:16:47:02 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 33.510983 \"-\" \"@list-item-updater\" prio:0");
            writer2.newLine();
            writer2.write("192.168.32.181 - - [14/06/2017:16:47:12 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 33.510983 \"-\" \"@list-item-updater\" prio:0");
            writer2.newLine();
            writer2.write("192.168.32.181 - - [14/06/2017:16:47:22 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 23.510983 \"-\" \"@list-item-updater\" prio:0");
            writer2.newLine();
            writer2.write("192.168.32.181 - - [14/06/2017:16:47:30 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 22.510983 \"-\" \"@list-item-updater\" prio:0");
            writer2.newLine();
            writer2.write("192.168.32.181 - - [14/06/2017:16:47:40 +1000] \"PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1\" 200 2 22.510983 \"-\" \"@list-item-updater\" prio:0");

        }
        try (BufferedReader reader2 = new BufferedReader(new FileReader(tempFile2))) {

            StringBuilder result = Main.aggregateLogsInterval(currentTime, endTime, reader2, availability, timeout, code, currentTimeout);
            String str = result.toString();
            assertEquals("", str);
        }

    }
}
