package com.bnppf.kata.online_book_store.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("book")
    private BookDTO book;
    @JsonProperty("quantity")
    private Integer quantity;
}
