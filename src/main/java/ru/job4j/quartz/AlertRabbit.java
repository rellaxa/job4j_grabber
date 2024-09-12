package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    private static final Properties PROPERTIES = new Properties();

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
            store.add(System.currentTimeMillis());

            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connect");
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO rabbit(created_date) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
    }

    private static Connection initConnect() {
        Connection connection;
        try (InputStream input = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            PROPERTIES.load(input);
            Class.forName(PROPERTIES.getProperty("driver_class"));
            connection = DriverManager.getConnection(
                    PROPERTIES.getProperty("url"),
                    PROPERTIES.getProperty("username"),
                    PROPERTIES.getProperty("password")
            );
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return connection;
    }

    public static void main(String[] args) {
        try {
            List<Long> store = new ArrayList<>();
            Connection connection = initConnect();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("store", store);
            data.put("connect", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();

            int interval = Integer.parseInt(PROPERTIES.getProperty("rabbit.interval"));
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();

            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
            connection.close();
            System.out.println(store);
        } catch (Exception se) {
            se.printStackTrace();
        }
    }
}