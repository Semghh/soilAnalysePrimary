package com.example.content2.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenStore {
    private String username;
    private String roles;
    private Long createTime;
}
