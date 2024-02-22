package com.example.bookswapplatform.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    private int page;
    private Long limit;
    private int totalPage;
}
