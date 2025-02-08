package com.bxacosta.wpcloudapi.controllers;

import com.bxacosta.wpcloudapi.services.WhatsAppBusinessService;
import com.restfb.types.whatsapp.WhatsAppBusinessAccount;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class IndexController {

    private final WhatsAppBusinessService whatsAppBusinessService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> index(HttpServletRequest request) {
        String accessToken = (String) request.getSession().getAttribute("access_token");

        if (accessToken == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "Not Authenticated"));

        List<String> wabaIds = whatsAppBusinessService.getSharedWABAIds(accessToken);

        if (wabaIds.isEmpty()) return ResponseEntity.noContent().build();

        List<WhatsAppBusinessAccount> accounts = wabaIds.stream().map(id -> whatsAppBusinessService.getWhatsAppBusinessAccount(accessToken, id)).toList();

        return ResponseEntity.ok(Map.of(
                "status", "Connected",
                "accounts", accounts
        ));
    }
}
