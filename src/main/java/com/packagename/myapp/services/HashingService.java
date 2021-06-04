package com.packagename.myapp.services;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

public class HashingService {
    public static String hashThis(String password) {
        if(StringUtils.isBlank(password)){
            return "";
        }
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}
