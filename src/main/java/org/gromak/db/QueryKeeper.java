package org.gromak.db;

import org.gromak.entity.Good;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueryKeeper {
    public ConcurrentLinkedQueue<Good> queue;

    public QueryKeeper() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void addQuery(Good good){
        System.out.println("Query added: " + good + " size" +queue.size());
        queue.add(good);
    }
}
