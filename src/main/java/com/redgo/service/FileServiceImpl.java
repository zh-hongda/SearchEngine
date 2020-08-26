package com.redgo.service;

import com.redgo.dao.FileDAO;
import com.redgo.entity.FileWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private FileDAO fileDAO;


    @Override
    public HashMap<String, Object> findAll(String queryString, String page, String powerStation, String unit, String docType){
        return fileDAO.findAll(queryString, page, powerStation,unit, docType);
    }

}
