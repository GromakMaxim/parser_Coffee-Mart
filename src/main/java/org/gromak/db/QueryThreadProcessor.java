package org.gromak.db;

import org.gromak.Options;
import org.gromak.entity.Good;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Creates an ExecutorService for working with QueryKeeper.
 * Checks its contents every second and, when detected, creates a runnable task (QueryExecutor) to add to the database.
 * If the storage does not receive data from parsing for a certain time, it stops working.
 */
public class QueryThreadProcessor {
    private QueryKeeper queryKeeper;

    public QueryThreadProcessor(QueryKeeper queryKeeper) {
        System.out.println("QueryProcessor is starting..");
        this.queryKeeper = queryKeeper;

        checkTables();
        runQuery();
    }

    private void checkTables() {
        try (Connection con = DriverManager.getConnection(Options.DBURL.getValue(), Options.DBUSER.getValue(), Options.DBPASSWORD.getValue())){
            String sql = "CREATE TABLE IF NOT EXISTS goods" +
                    "(" +
                    "id INT NOT NULL AUTO_INCREMENT," +
                    "code VARCHAR(30)," +
                    "title VARCHAR(100)," +
                    "price DOUBLE," +
                    "descr VARCHAR(2000)," +
                    "country VARCHAR(20)," +
                    "weight VARCHAR(20)," +
                    "producer VARCHAR(30)," +
                    "color VARCHAR(30)," +
                    "materialType VARCHAR(40)," +
                    "cardStatus VARCHAR(40)," +
                    "link VARCHAR(100)," +
                    "PRIMARY KEY (id)" +
                    ")";

           PreparedStatement preparedStatement = con.prepareStatement(sql);
           preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void runQuery() {
        int counter = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        while (true) {
            if (counter > 60) {
                System.out.println("QueryProcessor is shutting down..");
                executorService.shutdown();
                break;
            }

            try {
                Good g = queryKeeper.queue.poll();
                if (g == null) {
                    System.out.println("There is no task in queue.. I'm waiting: " + counter + "/60");
                    counter++;
                    Thread.sleep(1000);
                } else {
                    counter = 0;
                    QueryExecutor task = new QueryExecutor(g);
                    executorService.execute(task);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
