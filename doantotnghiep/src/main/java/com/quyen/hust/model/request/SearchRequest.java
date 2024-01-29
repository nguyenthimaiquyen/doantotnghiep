package com.quyen.hust.model.request;

import lombok.Data;

@Data
public class SearchRequest {
    private String name;
    private int currentPage = 0;
    private int pageSize = 6;

}
