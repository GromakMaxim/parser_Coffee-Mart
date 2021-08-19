package org.gromak.entity;

public class Good {
    private String code; //код позиции
    private String title; //название
    private int price; //цена
    private String description; //описание товара на 1 вкладке
    private String country; //страна происх
    private String weight; //вес
    private String producer;//произв
    private String color;//цвет
    private String materialType; //материал
    private String cardStatus; //скидка/промо/новинка
    private String link; //ссылка на этот товар

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    @Override
    public String toString() {
        return "Good{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", country='" + country + '\'' +
                ", weight='" + weight + '\'' +
                ", producer='" + producer + '\'' +
                ", color='" + color + '\'' +
                ", materialType='" + materialType + '\'' +
                ", cardStatus='" + cardStatus + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
