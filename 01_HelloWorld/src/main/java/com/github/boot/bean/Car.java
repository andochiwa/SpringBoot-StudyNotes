package com.github.boot.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 只有在容器中的组件才会拥有springboot提供的强大功能
 * @author HAN
 * @version 1.0
 * @create 03-04-5:54
 */
// @Component MyConfig自动绑定
@ConfigurationProperties(prefix = "mycar")
public class Car {

    private String brand;
    private Integer price;

    public Car() {
    }

    public Car(String brand, Integer price) {
        this.brand = brand;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", price=" + price +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
