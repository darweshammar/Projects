package globalServer;

import javax.swing.*;
import java.io.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**@author Malek Abdul Sater  @coauthor**/
public class Logger implements Serializable {
    private ArrayList<Long> timeStamps;
    private ArrayList<String> events;
    private String userName;
    private File eventLog;

    public Logger() {
        timeStamps = new ArrayList();
        events = new ArrayList();
    }

    public void deleteLog() {
        eventLog.delete();
    }

    public void createLogger(String userName) {
        this.userName = userName;
        eventLog = new File("logs/eventLog_" + this.userName);
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(eventLog)))) {
            bw.write("Time:\t\t\t\t\tEvent:");
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToLog(String event) {
        String text = "";
        Date date = new Date();
        java.sql.Timestamp stamp = new Timestamp(date.getTime());
        long currentTimeInSeconds = stamp.getTime();

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("logs/eventLog_" + this.userName, true)))) {
            {
                text += currentTimeInSeconds + "_" + event;
                bw.write(text);
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillArrayLists() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("logs/eventLog_" + this.userName)))) {
            String line = br.readLine();

            events.clear();
            timeStamps.clear();

            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    if (line.contains("_")) {
                        String[] temp = line.split("_");

                        String time = temp[0];
                        String event = temp[1];
                        events.add(event);
                        timeStamps.add(Long.parseLong(time));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLog(int startYear, int startMonth, int startDayOfMonth, int startHour, int startMinute,
                         int endYear, int endMonth, int endDayOfMonth, int endHour, int endMinute) {

        String res = "";
        Timestamp temp = null;
        Timestamp startingTime;
        Timestamp endingTime;

        fillArrayLists();

        LocalDateTime dateTimeStart = LocalDateTime.of(startYear, startMonth, startDayOfMonth, startHour, startMinute);
        startingTime = new Timestamp(1);
        startingTime = startingTime.valueOf(dateTimeStart);

        LocalDateTime dateTimeEnd = LocalDateTime.of(endYear, endMonth, endDayOfMonth, endHour, endMinute);
        endingTime = new Timestamp(1);
        endingTime = endingTime.valueOf(dateTimeEnd);

        for (int i = 0; i < events.size(); i++) {
            temp = new Timestamp(timeStamps.get(i));

            if (temp.after(startingTime) & temp.before(endingTime)) {
                res += (temp.toLocalDateTime() + ": " + events.get(i) + "\n");
            }
        }
        return res;
    }
}

