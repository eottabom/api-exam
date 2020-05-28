package com.eottabom.example.api.ui.dto;

import java.util.List;

public class ApiDto {

    public static class MaxAccountByYearResponse {
        private String year;
        private String name;
        private long acctNo;
        private long sumAmt;

        public MaxAccountByYearResponse() { }

        public MaxAccountByYearResponse(String year, String name, long acctNo, long sumAmt) {
            this.year = year;
            this.name = name;
            this.acctNo = acctNo;
            this.sumAmt = sumAmt;
        }

        public String getYear() {
            return year;
        }

        public String getName() {
            return name;
        }

        public long getAcctNo() {
            return acctNo;
        }

        public long getSumAmt() {
            return sumAmt;
        }

    }

    public static class NoTransactionByYearResponse {
        private String year;
        private String name;
        private long acctNo;

        public NoTransactionByYearResponse() { }

        public NoTransactionByYearResponse(String year, String name, long acctNo) {
            this.year = year;
            this.name = name;
            this.acctNo = acctNo;
        }

        public String getYear() {
            return year;
        }

        public String getName() {
            return name;
        }

        public long getAcctNo() {
            return acctNo;
        }

    }

    public static class AmountByYearResponse {
        private String year;
        private List<ManagementResponse> dataList;

        public AmountByYearResponse() { }

        public AmountByYearResponse(String year, List<ManagementResponse> dataList) {
            this.year = year;
            this.dataList = dataList;
        }

        public String getYear() {
            return year;
        }

        public List<ManagementResponse> getDataList() {
            return dataList;
        }

    }

    public static class ManagementResponse {
        private String brName;
        private String brCode;
        private long sumAmt;

        public ManagementResponse() { }

        public ManagementResponse(String brName, String brCode, long sumAmt) {
            this.brName = brName;
            this.brCode = brCode;
            this.sumAmt = sumAmt;
        }

        public String getBrName() {
            return brName;
        }

        public String getBrCode() {
            return brCode;
        }

        public long getSumAmt() {
            return sumAmt;
        }

    }

}
