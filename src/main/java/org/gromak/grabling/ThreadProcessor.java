package org.gromak.grabling;

import org.gromak.visitor.LinkVisitor;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadProcessor {
    private Set<String> links;

    public ThreadProcessor(Set<String> links) {
        this.links = links;
    }

    public void init(){
        ArrayList<ContentGrabber> tasks = defineTasks();
        runTasks(tasks);
    }

    private void runTasks(ArrayList<ContentGrabber> tasks) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for(ContentGrabber task : tasks){
            executorService.execute(task);
        }

        executorService.shutdown();
    }

    private ArrayList<ContentGrabber> defineTasks() {
        System.out.println("ThreadProcessor: set new tasks " + links);

        ArrayList<ContentGrabber> tasks = new ArrayList<>();
        for (String link : links) {
            System.out.println("ThreadProcessor: task created " + link);
            Document doc = new LinkVisitor().getRawHTML(link, true);
            tasks.add(new ContentGrabber(doc));
        }

        return tasks;
    }
}
