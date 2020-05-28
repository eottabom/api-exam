package com.eottabom.example.api.dao;

import com.eottabom.example.api.ui.dto.ApiDto.AmountByYearResponse;
import com.eottabom.example.api.ui.dto.ApiDto.ManagementResponse;
import com.eottabom.example.api.ui.dto.ApiDto.MaxAccountByYearResponse;
import com.eottabom.example.api.ui.dto.ApiDto.NoTransactionByYearResponse;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ApiDao {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public ApiDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("STATION")
                .usingGeneratedKeyColumns("ID");
    }

    public List<MaxAccountByYearResponse> maxAccountByYearResponses() {
        String sql = "SELECT DD.YEAR AS YEAR" +
                "           , DD.ACCOUNTNAME AS NAME" +
                "           , DD.ACCOUNTNUMBER AS ACCTNO" +
                "           , QQ.TT AS SUMAMT" +
                "  FROM (SELECT MAX(SUMAMOUNT) AS TT" +
                "           FROM ( SELECT TO_CHAR(DH.TRANSACTION_DATE, 'YYYY') AS YEAR" +
                "                        , AI.ACCOUNT_NUM AS ACCOUNTNUMBER" +
                "                        , SUM(DH.AMOUNT - DH.FEE) AS SUMAMOUNT" +
                "                    FROM TRANSACTION_DETAIL DH" +
                "                   INNER JOIN ACCOUNT AI ON DH.ACCOUNT_NUM = AI.ACCOUNT_NUM" +
                "                   WHERE DH.CANCEL_YN = 'N'" +
                "                   GROUP BY TO_CHAR(DH.TRANSACTION_DATE, 'YYYY'), AI.ACCOUNT_NUM" +
                "               ) E" +
                "           GROUP BY E.YEAR" +
                "       ) QQ" +
                " INNER JOIN (SELECT TO_CHAR(DH.TRANSACTION_DATE, 'YYYY') AS YEAR" +
                "                    , AI.ACCOUNT_NUM AS ACCOUNTNUMBER" +
                "                    , AI.ACCOUNT_NAME AS ACCOUNTNAME" +
                "                    , SUM(DH.AMOUNT - DH.FEE) AS SUMAMOUNT" +
                "                FROM TRANSACTION_DETAIL DH" +
                "               INNER JOIN ACCOUNT AI ON DH.ACCOUNT_NUM = AI.ACCOUNT_NUM" +
                "               WHERE DH.CANCEL_YN = 'N' " +
                "               GROUP BY TO_CHAR(DH.TRANSACTION_DATE, 'YYYY'), AI.ACCOUNT_NUM, AI.ACCOUNT_NAME" +
                "        ) DD " +
                "    ON QQ.TT = DD.SUMAMOUNT";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new MaxAccountByYearResponse(
                                rs.getString("year"),
                                rs.getString("name"),
                                rs.getLong("acctNo"),
                                rs.getLong("sumAmt")
                        )
        );
    }

    public List<NoTransactionByYearResponse> noTransactionByYear() {
        String sql = "SELECT DISTINCT TO_CHAR(TD.TRANSACTION_DATE, 'YYYY') AS YEAR" +
                "            , AI.ACCOUNT_NUM AS ACCTNO" +
                "            , AI.ACCOUNT_NAME AS NAME" +
                "       FROM ACCOUNT AI" +
                "      INNER JOIN TRANSACTION_DETAIL TD ON AI.ACCOUNT_NUM = TD.ACCOUNT_NUM" +
                "      WHERE TD.CANCEL_YN = 'N'" +
                "        AND AI.ACCOUNT_NUM NOT IN (" +
                "           (SELECT DISTINCT TD.ACCOUNT_NUM AS ACCOUNTNUMBER" +
                "              FROM TRANSACTION_DETAIL TD" +
                "             WHERE TD.CANCEL_YN = 'N'" +
                "                AND (TO_CHAR(TD.TRANSACTION_DATE, 'YYYY-MM-DD') BETWEEN TO_DATE('2019-01-01', 'YYYY-MM-DD') AND TO_DATE('2019-12-31','YYYY-MM-DD'))" +
                "           )" +
                "       )" +
                "         OR AI.ACCOUNT_NUM NOT IN (" +
                "               (SELECT DISTINCT TD.ACCOUNT_NUM AS ACCOUNTNUMBER" +
                "                  FROM TRANSACTION_DETAIL TD" +
                "                 WHERE TD.CANCEL_YN = 'N'" +
                "                   AND (TO_CHAR(TD.TRANSACTION_DATE, 'YYYY-MM-DD') BETWEEN TO_DATE('2018-01-01', 'YYYY-MM-DD') AND TO_DATE('2018-12-31','YYYY-MM-DD'))" +
                "              )" +
                "     )";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        new NoTransactionByYearResponse(
                                rs.getString("year"),
                                rs.getString("name"),
                                rs.getLong("acctNo")
                        )
        );
    }

     public List<AmountByYearResponse> amountByYear() {
        String sql = "SELECT TO_CHAR(TD.TRANSACTION_DATE, 'YYYY') AS YEAR" +
                "           , MG.MANAGEMENT_NAME AS BRNAME" +
                "           , MG.MANAGEMENT_CODE AS BRCODE" +
                "           , SUM(TD.AMOUNT-TD.FEE) AS SUMAMT" +
                "       FROM TRANSACTION_DETAIL TD, ACCOUNT AI, MANAGEMENT MG" +
                "      WHERE TD.ACCOUNT_NUM = AI.ACCOUNT_NUM" +
                "        AND AI.MANAGEMENT_CODE = MG.MANAGEMENT_CODE" +
                "        AND TD.CANCEL_YN = 'N'" +
                "      GROUP BY TO_CHAR(TD.TRANSACTION_DATE, 'YYYY'), MG.MANAGEMENT_NAME, MG.MANAGEMENT_CODE" +
                "      ORDER BY YEAR, SUMAMT DESC";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<String, List<Map<String, Object>>> resultByYear = result.stream().collect(Collectors.groupingBy(it -> (String) it.get("year")));


        return resultByYear.entrySet().stream()
                .map(it -> mapAmount(it.getValue()))
                .collect(Collectors.toList());

    }

    private AmountByYearResponse mapAmount(List<Map<String, Object>> result) {
        List<ManagementResponse> managementResponses = mapManagement(result);

        return new AmountByYearResponse(
                (String) result.get(0).get("year"),
                managementResponses
        );
    }

    public List<ManagementResponse> mapManagement(List<Map<String, Object>> result) {
        return result.stream()
                .collect(Collectors.groupingBy(it -> it.get("year")))
                .entrySet()
                .stream()
                .map(it ->
                        new ManagementResponse(
                                (String) result.get(0).get("brName"),
                                (String) result.get(0).get("brCode"),
                                (long) result.get(0).get("sumAmt")
                        )
                )
                .collect(Collectors.toList());
    }

    public ManagementResponse totalTransactionByManagement(String brName) {
        String sql = "SELECT A.SUMAMOUT AS SUMAMT" +
                "           , A.MANAGEMENT_NAME AS BRNAME" +
                "           , A.MANAGEMENT_CODE AS BRCODE" +
                "       FROM (" +
                "              SELECT SUM(TD.AMOUNT) AS SUMAMOUT," +
                "                      REPLACE(MG.MANAGEMENT_NAME, '분당점', '판교점') AS MANAGEMENT_NAME," +
                "                      REPLACE(MG.MANAGEMENT_CODE, 'B', 'A') AS MANAGEMENT_CODE" +
                "                FROM TRANSACTION_DETAIL TD, ACCOUNT AI, MANAGEMENT MG" +
                "               WHERE TD.ACCOUNT_NUM = AI.ACCOUNT_NUM" +
                "                 AND AI.MANAGEMENT_CODE = MG.MANAGEMENT_CODE" +
                "                 AND TD.CANCEL_YN = 'N'" +
                "               GROUP BY REPLACE(MG.MANAGEMENT_NAME, '분당점', '판교점'), REPLACE(MG.MANAGEMENT_CODE, 'B', 'A')) A" +
                "               WHERE MANAGEMENT_NAME = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, new Object[]{brName});

        if(result.isEmpty()) {
            JsonObject httpResponse = new JsonObject();
            httpResponse.addProperty("code", "404");
            httpResponse.addProperty("메시지", "br code not found error");

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpResponse.toString());
        }

        return new ManagementResponse(
                (String) result.get(0).get("brName"),
                (String) result.get(0).get("brCode"),
                (long) result.get(0).get("sumAmt")
         );
    }

}






