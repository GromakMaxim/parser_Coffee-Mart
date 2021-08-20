package org.gromak.entity;

/**builder pattern*/
public final class GoodBuilder {
    private Good g = new Good();

    public void reset() {
        g = new Good();
    }

    public Good build() {
        return g;
    }

    public GoodBuilder withCode(String code) {
        g.setCode(code);
        return this;
    }

    public GoodBuilder withTitle(String title) {
        g.setTitle(title);
        return this;
    }

    public GoodBuilder withPrice(int price) {
        g.setPrice(price);
        return this;
    }

    public GoodBuilder withDescription(String description) {
        g.setDescription(description);
        return this;
    }

    public GoodBuilder withCountry(String country) {
        g.setCountry(country);
        return this;
    }

    public GoodBuilder withWeight(String weight) {
        g.setWeight(weight);
        return this;
    }

    public GoodBuilder withProducer(String producer) {
        g.setProducer(producer);
        return this;
    }

    public GoodBuilder withColor(String color) {
        g.setColor(color);
        return this;
    }

    public GoodBuilder withMaterialType(String materialType) {
        g.setMaterialType(materialType);
        return this;
    }

    public GoodBuilder withCardStatus(String cardStatus) {
        g.setCardStatus(cardStatus);
        return this;
    }

    public GoodBuilder withLink(String link) {
        g.setLink(link);
        return this;
    }

}
