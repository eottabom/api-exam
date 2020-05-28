package com.eottabom.example.api.application;

import com.eottabom.example.api.dao.ApiDao;
import com.eottabom.example.api.ui.dto.ApiDto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiService {

    private ApiDao apiDao;

    public ApiService(ApiDao apiDao) {
        this.apiDao = apiDao;
    }

    public List<MaxAccountByYearResponse> findMaxAccountByYear() {
        return apiDao.maxAccountByYearResponses();
    }

    public List<NoTransactionByYearResponse> findNoTransactionByYear() {
        return apiDao.noTransactionByYear();
    }

    public List<AmountByYearResponse> findAmountByYear() {
        return apiDao.amountByYear();
    }

    public ManagementResponse findTotalTransactionByManagement(String brName) {
        return apiDao.totalTransactionByManagement(brName);
    }

}
