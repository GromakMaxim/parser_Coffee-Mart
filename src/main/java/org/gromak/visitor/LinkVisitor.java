package org.gromak.visitor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**Performs 'clicking' on links and getting raw html*/
public class LinkVisitor {
    private String userAgent;
    private String referrer;

    public LinkVisitor() {
        this.userAgent = "Chrome/4.0.249.0 Safari/532.5";
        this.referrer = "https://www.google.com";
    }

    public Document getRawHTML(String link, boolean allInOnePage){
        if (allInOnePage) {
            link = link + "?size=5000";
        }

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
