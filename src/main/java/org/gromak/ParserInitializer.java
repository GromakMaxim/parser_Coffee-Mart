package org.gromak;

import org.gromak.grabling.ThreadProcessor;
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

    public Set<String> getAllEndpoints() {
        return allEndpoints;
    }

    public Set<String> getMainGroupsEndpoints() {
        return mainGroupsEndpoints;
    }

    public Set<String> getSubGroupsEndpoints() {
        return subGroupsEndpoints;
    }

    public ParserInitializer() {
        System.out.println("ParserInitializer called LinkVisitor: " + mainLink + " false");
        document = new LinkVisitor().getRawHTML(mainLink, false);
    }

    public void init() {
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
        new ThreadProcessor(mainGroupsEndpoints).init();
    }
}
