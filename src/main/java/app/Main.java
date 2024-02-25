package app;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("u", true, "availability percent");
        options.addOption("t", true, "timeout allow");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            double availability = Double.parseDouble(cmd.getOptionValue("u"));
            int timeout = Integer.parseInt(cmd.getOptionValue("t"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineArr = line.split(" ");
                String code = lineArr[8];
                int startIndex = lineArr[3].indexOf(":") + 1;
                String timeStr = lineArr[3].substring(startIndex);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                try {
                    Date time = sdf.parse(timeStr);
                    Date endTime = new Date(time.getTime() + TimeUnit.MINUTES.toMillis(1));
                    int currentTimeout = (int) Double.parseDouble(lineArr[10]);
                    StringBuilder res = aggregateLogsInterval(time, endTime, reader, availability, timeout, code, currentTimeout);
                    if (!res.isEmpty()) {
                        System.out.println(res.toString());
                    }

                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder aggregateLogsInterval(Date currentTime,
                                     Date endTime,
                                     BufferedReader reader,
                                     double availability,
                                     int allowedTimeout,
                                     String currentRequestCode,
                                     int currentTimeout) {
        String line;
        String regex = "^5\\d{2}$";
        double countRequest = 1;
        double countGoodRequest = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        DecimalFormat df = new DecimalFormat("#.#");
        StringBuilder sb = new StringBuilder();
        if (!currentRequestCode.matches(regex) && currentTimeout <= allowedTimeout) {
           countGoodRequest++;
        }
        try {
            while ((line = reader.readLine()) != null && endTime.after(currentTime)) {
                String[] lineArr = line.split(" ");
                currentTimeout = (int) Double.parseDouble(lineArr[10]);
                currentRequestCode = lineArr[8];
                int startIndex = lineArr[3].indexOf(":") + 1;
                String timeStr = lineArr[3].substring(startIndex);

                try {
                    currentTime = sdf.parse(timeStr);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                if (!currentRequestCode.matches(regex) && currentTimeout <= allowedTimeout) {
                    countGoodRequest++;
                }
                countRequest++;
            }
            double percentAvailability = (countGoodRequest / countRequest) * 100;
            if (percentAvailability < availability) {
                sb.append(sdf.format(currentTime));
                sb.append("  ");
                sb.append(sdf.format(endTime));
                sb.append("  ");
                sb.append(df.format(percentAvailability));

                return sb;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }
}