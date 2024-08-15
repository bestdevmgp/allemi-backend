package org.example.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

public class NeisApiService {

    private static final String NEIS_API_URL = "https://open.neis.go.kr/hub/mealServiceDietInfo";
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String getMealInfo(String atptOfcdcScCode, String sdSchulCode, String key) throws Exception {
        // 현재 날짜를 8자리 정수로 포맷
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(Calendar.getInstance().getTime());

        // API 요청 파라미터 구성
        Map<String, String> params = Map.of(
                "ATPT_OFCDC_SC_CODE", "D10",
                "SD_SCHUL_CODE", "7240059",
                "KEY", "c929b93c4bc4439285cfecec54ea9632",
                "MLSV_YMD", currentDate
        );

        String paramString = params.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        // 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(NEIS_API_URL + "?" + paramString))
                .GET()
                .build();

        // API 호출
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // API 응답 반환
        return response.body();
    }

    public static void main(String[] args) {
        NeisApiService service = new NeisApiService();
        try {
            // 실제 값으로 교체
            String response = service.getMealInfo("YOUR_ATPT_OFCDC_SC_CODE", "YOUR_SD_SCHUL_CODE", "c929b93c4bc4439285cfecec54ea9632");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}