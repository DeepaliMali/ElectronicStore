package com.deepali.electronicstore.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductDto {


    private String productId;

    private String title;

    private String description;

    private int price;

    private int discountedPrice;

    private int quantity;

    private Date addedDate;

    private boolean live;

    private boolean stock;




}
