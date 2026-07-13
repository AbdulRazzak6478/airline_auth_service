package com.airline.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchListRequest {

    private Integer page = 0;

    private Integer limit = 10;

    private String sortDirection = "ASEC";

    public String sortBy = "createdAt";
}
