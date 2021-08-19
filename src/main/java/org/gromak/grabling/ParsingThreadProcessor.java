package org.gromak.grabling;

import org.gromak.db.QueryKeeper;
import org.gromak.visitor.LinkVisitor;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParsingThreadProcessor {
    private Set<String> links;
    private QueryKeeper queryKeeper;

    public ParsingThreadProcessor(Set<String> links, QueryKeeper queryKeeper) {
        this.links = links;
        this.queryKeeper = queryKeeper;

        ArrayList<ContentGrabber> tasks = defineTasks();
        runTasks(tasks);
    }

    private void runTasks(ArrayList<ContentGrabber> tasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (ContentGrabber task : tasks) {
            executorService.execute(task);
        }
        executorService.shutdown();
    }

    private ArrayList<ContentGrabber> defineTasks() {
        ArrayList<ContentGrabber> tasks = new ArrayList<>();
        for (String link : links) {
            Document doc = new LinkVisitor().getRawHTML(link, true);
            tasks.add(new ContentGrabber(doc, queryKeeper));
        }
        return tasks;
    }
}
