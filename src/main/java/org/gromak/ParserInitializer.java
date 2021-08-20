package org.gromak;

import org.gromak.db.QueryKeeper;
import org.gromak.db.QueryThreadProcessor;
import org.gromak.grabling.ParsingThreadProcessor;
import org.gromak.visitor.LinkVisitor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Set;

/**
 * Gets links to product groups from the main page of the site.
 * Sends the received links to the ParsingThreadProcessor for processing.
 * Starts the QueryThreadProcessor so that it waits for the results from the ParsingThreadProcessor in QueryKeeper.
 */
public class ParserInitializer {
    private Document document; //raw html from main page
    private String mainLink = "https://www.coffee-mart.ru";

    private Set<String> allEndpoints = new HashSet<>();
    private Set<String> mainGroupsEndpoints = new HashSet<>();
    private Set<String> subGroupsEndpoints = new HashSet<>();

    private QueryKeeper queryKeeper = new QueryKeeper(); //stores the found products

    public ParserInitializer() {
        document = new LinkVisitor().getRawHTML(mainLink, false);
    }

    public void init() {
        findRefsFromMainMenu();
        sendLinkSetToExecution();
    }

    private void findRefsFromMainMenu() {
        var elements = document
                .select(".header-main-nav")
                .select("ul")
                .select("li")
                .select("a[href]");

        for (Element e : elements) {
            String endpoint = e.attr("href");
            String foundLink = mainLink + endpoint;
            allEndpoints.add(foundLink);

            String[] arr = endpoint.split("/");
            if (arr.length == 3) mainGroupsEndpoints.add(mainLink + endpoint);
            else subGroupsEndpoints.add(mainLink + endpoint);
        }
    }

    public void sendLinkSetToExecution() {
        new ParsingThreadProcessor(mainGroupsEndpoints, queryKeeper);
        new QueryThreadProcessor(queryKeeper);
    }
}
