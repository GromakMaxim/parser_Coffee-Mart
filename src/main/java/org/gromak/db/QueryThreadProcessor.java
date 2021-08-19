package org.gromak.db;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.gromak.entity.Good;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueryThreadProcessor {
    private QueryKeeper queryKeeper;

    public QueryThreadProcessor(QueryKeeper queryKeeper) {
        System.out.println("QueryProcessor is starting..");
        this.queryKeeper = queryKeeper;

        runQuery();
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
                    System.out.println("there is no task in queue.. iam waiting: " + counter +"/60");
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
