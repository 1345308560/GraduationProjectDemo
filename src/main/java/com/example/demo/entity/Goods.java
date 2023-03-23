package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String title;

    private String uid;

    private Integer degree;

    private Integer type;

    private BigDecimal price;

    private BigDecimal price_ago;

    private Integer num;

    private String description;

}
