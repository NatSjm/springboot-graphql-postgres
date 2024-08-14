package com.hillel.ua.graphql.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {
    private int page;
    private int size;
}
