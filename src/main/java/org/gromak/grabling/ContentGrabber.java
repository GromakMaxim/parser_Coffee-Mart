package org.gromak.grabling;

import org.gromak.entity.Good;
import org.gromak.entity.GoodBuilder;
import org.gromak.visitor.LinkVisitor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContentGrabber implements Runnable {
    private Document document;
    private HashMap<String, Good> goods = new HashMap<>();


    public ContentGrabber(Document document) {
        this.document = document;
    }

    @Override
    public void run() {
        GoodBuilder gb = new GoodBuilder();
        var containers = document.select("div.prod-item-container");

        for (Element container : containers) {
            String title = parseTitle(container);
            String status = parseCardStatus(container);
            String cardEndpoint = parseCardIndividualLink(container);
            String cardLink = "https://www.coffee-mart.ru" + cardEndpoint;
            int price = parsePrice(container);

            gb.withTitle(title);
            gb.withCardStatus(status);
            gb.withPrice(price);
            gb.withLink(cardLink);

            Document doc = new LinkVisitor().getRawHTML(cardLink, false);
            parseContainerContent(gb, doc);




            Good good = gb.build();
            System.out.println(good);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            goods.put(good.getCode(), good);
            gb.reset();
        }

    }

    private String parseTitle(Element container) {
        return container.select(".prod-title").select("a").text();
    }

    private String parseCardStatus(Element container) {
        List<String> cardStatusClasses;
        try {
            cardStatusClasses = new ArrayList<>(container
                    .select("div.prod-status")
                    .get(0)
                    .classNames());

            if (cardStatusClasses.size() > 1) return cardStatusClasses.get(1);
        } catch (Exception exception) {
            return "";
        }

        return "";
    }

    //получить ссылку на страничку товара
    private String parseCardIndividualLink(Element container) {
        return container
                .select("div.prod-title")
                .select("a")
                .attr("href");
    }

    private int parsePrice(Element container) {
        try {
            int price = Integer.parseInt(
                    container
                            .select(".price")
                            .select("meta")
                            .get(0)
                            .attr("content")
            );
            return price;
        } catch (Exception e) {
            try {
                int price = Integer.parseInt(
                        container
                                .select("div.price")
                                .text()
                                .trim()
                                .replace("&nbsp;", "")
                                .replace(" ", "")
                                .replace("c", ""));
                return price;
            } catch (Exception exception) {
                return 0;
            }
        }
    }

    private void parseContainerContent(GoodBuilder gb, Document document) {
        Elements infoList = document.select("ul.prod-info-list");
        Elements tabs = document.select("div.tab-content");

        String code = parseCode(infoList);
        String country = parseCountry(infoList);
        String producer = parseProducer(infoList);
        String description = parseDescription(tabs);

        String weight = getSpecFromSpecificationsList(tabs, "Вес");
        String color = getSpecFromSpecificationsList(tabs, "Цвет");
        String materialType = getSpecFromSpecificationsList(tabs, "Упаковка");

        gb.withCode(code);
        gb.withCountry(country);
        gb.withProducer(producer);
        gb.withDescription(description);

        gb.withWeight(weight);
        gb.withColor(color);
        gb.withMaterialType(materialType);
    }

    private String parseCode(Elements infoList){
        return infoList.select("li").get(0).select("span").text();
    }

    private String parseCountry(Elements infoList){
        return infoList.select("li").get(1).select("span").text();
    }

    private String parseProducer(Elements infoList){
        String tempStr = infoList.select("li").get(2).select("span").text();
        return tempStr.split(" ")[2];
    }

    private String parseDescription(Elements tabs) {
        try {
            return tabs
                    .select("#tab-1")
                    .first()
                    .select("p")
                    .first()
                    .text();
        } catch (Exception exception) {
            return "";
        }
    }

    private String getSpecFromSpecificationsList(Elements tabs, String target) {
        try {
            Elements specifications = tabs
                    .select("#tab-2")
                    .select("dl");

            for (Element spec : specifications) {
                var specName = spec
                        .select("dt")
                        .first().text()
                        .trim();

                if (specName.equalsIgnoreCase(target)) {
                    var specValue = spec
                            .select("dt")
                            .select("dd")
                            .first()
                            .text()
                            .trim();
                    return specValue;
                }

            }
            return "";
        } catch (Exception exception) {
            return "";
        }
    }
}
