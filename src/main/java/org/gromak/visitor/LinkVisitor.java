package org.gromak.visitor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.SQLOutput;

/*
* класс посещает ссылку переданную в конструкторе. Имеет 1 метод, возвращающий html разметку
* */
public class LinkVisitor {
    private String userAgent;
    private String referrer;

    public LinkVisitor() {
        this.userAgent = "Chrome/4.0.249.0 Safari/532.5";
        this.referrer = "https://www.google.com";
    }


    public Document getRawHTML(String link, boolean allInOnePage){
        System.out.println("LinkVisitor: .getRawHTML was called " + link + " " + allInOnePage);
        if (allInOnePage) {
            link = link + "?size=5000";
        }

        System.out.println("LinkVisitor: link now " + link) ;

        try {
            return Jsoup.connect(link)
                    .userAgent(userAgent)
                    .referrer(referrer)
                    .get();
        } catch (IOException exception) {
            return new Document("");
        }
    }
}
