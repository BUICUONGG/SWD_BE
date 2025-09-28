package swd.fpt.exegroupingmanagement.repository.httpclient;

import feign.QueryMap;
import swd.fpt.exegroupingmanagement.dto.request.ExchangeTokenRequest;
import swd.fpt.exegroupingmanagement.dto.response.ExchangeTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "outbound-auth", url = "https://oauth2.googleapis.com")
public interface OutboundAuthClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest exchangeTokenRequest);
}