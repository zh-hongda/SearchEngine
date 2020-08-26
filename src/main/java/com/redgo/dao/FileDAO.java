package com.redgo.dao;

import com.redgo.entity.FileWord;

import java.util.HashMap;
import java.util.List;

public interface FileDAO {
    public HashMap<String, Object> findAll(String queryString, String page, String powerStation, String unit, String docType);
}
