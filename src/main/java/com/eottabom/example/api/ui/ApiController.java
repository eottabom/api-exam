package com.eottabom.example.api.ui;

import com.eottabom.example.api.application.ApiService;
import com.eottabom.example.api.ui.dto.ApiDto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/customer/max-account")
    public List<MaxAccountByYearResponse> getMaxAccountByYear() {
        return apiService.findMaxAccountByYear();
    }

    @GetMapping("/customer/no-transaction")
    public List<NoTransactionByYearResponse> getNoTransactionByYear() {
        return apiService.findNoTransactionByYear();
    }

    @GetMapping("/management/sum-amt")
    public List<AmountByYearResponse> getAmountByYear() {
        return apiService.findAmountByYear();
    }

    @GetMapping("/management/total-transaction/{brName}")
    public ManagementResponse getTotalTransaction(@Valid @PathVariable String brName) {
        return apiService.findTotalTransactionByManagement(brName);
    }
}
