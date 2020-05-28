package com.eottabom.example.api.ui;

import com.eottabom.example.api.ui.dto.ApiDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import support.test.ApiWebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
public class ApiAcceptanceTest {

    private final static int PORT = 9090;
    private ApiWebTestClient client;

    @Autowired
    public WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.client = ApiWebTestClient.of(PORT);
    }

    @Test
    @DisplayName("연도별 합계 금액이 많은 고객을 추출 요청한다.")
    void getMaxAccountByYear() {
        client.getResource("/api/customer/max-account", null);
        List<ApiDto.MaxAccountByYearResponse> maxAccountByYearResponses
                = client.getResources("/api/customer/max-account", ApiDto.MaxAccountByYearResponse.class);
        assertThat(maxAccountByYearResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("연도별 거래가 없는 고객 추출 요청")
    void getNoTransactionByYear() {
        client.getResource("/api/customer/no-transaction", null);
        List<ApiDto.NoTransactionByYearResponse> noTransactionByYearResponses
                = client.getResources("/api/customer/no-transaction", ApiDto.NoTransactionByYearResponse.class);
        assertThat(noTransactionByYearResponses.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("연도별 관리점별 거래 합계 금액 추출 요청")
    void getAmountByYear() {
        client.getResource("/api/management/sum-amt", null);
        List<ApiDto.AmountByYearResponse> amountByYearResponses
                = client.getResources("/api/management/sum-amt", ApiDto.AmountByYearResponse.class);
        assertThat(amountByYearResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("해당 지점의 거래 금액 합계 출력 요청")
    void getManagementResponse() {
        client.getResource("/api/management/total-transaction/판교점", null);
        List<ApiDto.ManagementResponse> managementResponses
                = client.getResources("/api/management/total-transaction/판교점", ApiDto.ManagementResponse.class);
        assertThat(managementResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("해당 지점의 거래 금액 합계 출력 요청시 Not found")
    void getManagementResponseNotFound() {
        webTestClient.get().uri("/api/management/total-transaction/분당점")
                .exchange().expectStatus().isNotFound();
    }

}
