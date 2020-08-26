package com.redgo.service;

import com.redgo.entity.FileWord;

import java.util.HashMap;
import java.util.List;

public interface FileService {

    public HashMap<String, Object> findAll(String queryString, String page,String powerStation, String unit, String docType);
}
