package com.bxacosta.wpcloudapi.services;

import com.bxacosta.wpcloudapi.auth.FacebookAuthService;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.scope.FacebookPermissions;
import com.restfb.types.DebugTokenInfo;
import com.restfb.types.GranularScope;
import com.restfb.types.whatsapp.WhatsAppBusinessAccount;
import com.restfb.types.whatsapp.WhatsAppBusinessPhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhatsAppBusinessService {

    private final FacebookAuthService facebookAuthService;

    public List<String> getSharedWABAIds(String accessToken) {
        DebugTokenInfo tokenInfo = facebookAuthService.debugToken(accessToken, accessToken);

        return tokenInfo.getGranularScopes().stream()
                .filter(granularScope -> FacebookPermissions.WHATSAPP_BUSINESS_MANAGEMENT.getPermissionString()
                        .equalsIgnoreCase(granularScope.getScope()))
                .findFirst()
                .map(GranularScope::getTargetIds)
                .orElse(new ArrayList<>());
    }

    public WhatsAppBusinessAccount getWhatsAppBusinessAccount(String accessToken, String wabaId) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);

        return facebookClient.fetchObject(wabaId, WhatsAppBusinessAccount.class);
    }

    public List<WhatsAppBusinessPhoneNumber> getPhoneNumbersByWABAId(String accessToken, String wabaId) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);

        Connection<WhatsAppBusinessPhoneNumber> phoneNumberConnection = facebookClient.fetchConnection(
                wabaId + "/phone_numbers",
                WhatsAppBusinessPhoneNumber.class
        );

        List<WhatsAppBusinessPhoneNumber> phoneNumbers = new ArrayList<>();
        phoneNumberConnection.forEach(phoneNumbers::addAll);
        return phoneNumbers;
    }
}
