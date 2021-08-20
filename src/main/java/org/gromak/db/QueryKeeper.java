package org.gromak.db;

import org.gromak.entity.Good;
import java.util.concurrent.ConcurrentLinkedQueue;

/**Storage for thread-safe work with received good*/
public class QueryKeeper {
    public ConcurrentLinkedQueue<Good> queue;

    public QueryKeeper() {
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public void addQuery(Good good){
        queue.add(good);
    }
}
