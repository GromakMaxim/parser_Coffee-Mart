package org.gromak;

import org.gromak.db.QueryKeeper;
import org.gromak.db.QueryThreadProcessor;
import org.gromak.grabling.ParsingThreadProcessor;
import org.gromak.visitor.LinkVisitor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashSet;
import java.util.Set;

public class ParserInitializer {
    private Document document;
    private String mainLink = "https://www.coffee-mart.ru";

    private Set<String> allEndpoints = new HashSet<>();
    private Set<String> mainGroupsEndpoints = new HashSet<>();
    private Set<String> subGroupsEndpoints = new HashSet<>();

    private QueryKeeper queryKeeper = new QueryKeeper();

    public ParserInitializer() {
        document = new LinkVisitor().getRawHTML(mainLink, false);
    }

    public void init() {
        System.out.println("ParserInitializer started");
        findRefsFromMainMenu();
        sendLinkSetToExecution();
    }

    private void findRefsFromMainMenu() {
        //найти все ссылки на группы товаров
        var elements = document
                .select(".header-main-nav")
                .select("ul")
                .select("li")
                .select("a[href]");

        //распределяем полученные ссылки: ссылка на группу товара или товарную подгруппу
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
