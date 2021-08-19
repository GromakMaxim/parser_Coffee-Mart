package org.gromak.grabling;

import org.gromak.db.QueryKeeper;
import org.gromak.entity.Good;
import org.gromak.entity.GoodBuilder;
import org.gromak.visitor.LinkVisitor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ContentGrabber implements Runnable {
    private Document document;
    private QueryKeeper queryKeeper;

    public ContentGrabber(Document document, QueryKeeper queryKeeper) {
        this.document = document;
        this.queryKeeper = queryKeeper;
    }

    @Override
    public void run() {
        GoodBuilder gb = new GoodBuilder();
        var containers = document.select("div.prod-item-container");

        for (Element container : containers) {
            String title = findTitle(container);
            String status = findCardStatus(container);
            String cardEndpoint = findCardIndividualLink(container);
            String cardLink = "https://www.coffee-mart.ru" + cardEndpoint;
            int price = findPrice(container);

            gb.withTitle(title);
            gb.withCardStatus(status);
            gb.withPrice(price);
            gb.withLink(cardLink);

            Document doc = new LinkVisitor().getRawHTML(cardLink, false);
            findContainerContent(gb, doc);

            Good good = gb.build();
            queryKeeper.addQuery(good);
            gb.reset();
        }
    }

    private String findTitle(Element container) {
        try {
            return container.select(".prod-title").select("a").text();
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    private String findCardStatus(Element container) {
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
    private String findCardIndividualLink(Element container) {
        try {
            return container
                    .select("div.prod-title")
                    .select("a")
                    .attr("href");
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    private int findPrice(Element container) {
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

    private void findContainerContent(GoodBuilder gb, Document document) {
        Elements infoList = document.select("ul.prod-info-list");
        Elements tabs = document.select("div.tab-content");

        String code = findCode(infoList);
        String country = findCountry(infoList);
        String producer = findProducer(infoList);
        String description = findDescription(tabs);

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

    private String findCode(Elements infoList) {
        try {
            return infoList.select("li").get(0).select("span").text();
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }

    }

    private String findCountry(Elements infoList) {
        try {
            return infoList.select("li").get(1).select("span").text();
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    private String findProducer(Elements infoList) {
        try {
            String tempStr = infoList.select("li").get(2).select("span").text();
            return tempStr.split(" ")[2];
        } catch (Exception exception) {
            exception.printStackTrace();
            return "";
        }
    }

    private String findDescription(Elements tabs) {
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
