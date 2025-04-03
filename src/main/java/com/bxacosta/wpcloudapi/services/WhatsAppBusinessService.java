package com.bxacosta.wpcloudapi.services;

import com.bxacosta.wpcloudapi.auth.FacebookAuthService;
import com.restfb.Body;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.json.JsonObject;
import com.restfb.scope.FacebookPermissions;
import com.restfb.types.DebugTokenInfo;
import com.restfb.types.GranularScope;
import com.restfb.types.whatsapp.WhatsAppBusinessAccount;
import com.restfb.types.whatsapp.WhatsAppBusinessPhoneNumber;
import com.restfb.types.whatsapp.WhatsAppMessageTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhatsAppBusinessService {

    private final FacebookAuthService facebookAuthService;

    /**
     * Retrieves the WhatsApp Business Account (WABA) IDs shared with the user, using the access token obtained
     * after completing the signup flow see
     * <a href="https://developers.facebook.com/docs/whatsapp/embedded-signup/manage-accounts#get-shared-waba-id-with-access-token">
     * Get Shared WABA ID with Access Token</a>.
     *
     * @param accessToken the access token obtained after signup
     * @return a list of shared WABA IDs, or an empty list if none are found
     */
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

    /**
     * Retrieves a list of WhatsApp Business phone numbers associated with a given WABA ID, see
     * <a href="https://developers.facebook.com/docs/graph-api/reference/whats-app-business-account/phone_numbers/">
     * WhatsApp Business Account Phone Numbers</a>.
     *
     * @param accessToken The access token used to authenticate the Facebook API.
     * @param wabaId      The ID of the WhatsApp Business Account (WABA) for which the phone numbers are retrieved.
     * @return A list of {@link WhatsAppBusinessPhoneNumber}.
     */
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

    /**
     * Retrieves a list of WhatsApp message templates associated with a specific WABA ID, see
     * <a href="https://developers.facebook.com/docs/graph-api/reference/whats-app-business-account/message_templates/">
     * WhatsApp Business Account Message Templates</a>.
     *
     * @param accessToken The access token used to authenticate the Facebook API.
     * @param wabaId      The ID of the WhatsApp Business Account (WABA) for which the message templates are retrieved.
     * @return A list of {@link WhatsAppMessageTemplate}.
     */
    public List<WhatsAppMessageTemplate> getMessageTemplatesByWABAId(String accessToken, String wabaId) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);

        Connection<WhatsAppMessageTemplate> templateConnection = facebookClient.fetchConnection(
                wabaId + "/message_templates",
                WhatsAppMessageTemplate.class
        );

        List<WhatsAppMessageTemplate> templates = new ArrayList<>();
        templateConnection.forEach(templates::addAll);
        return templates;
    }

    /**
     * Register a phone number to use the WhatsApp Business Cloud API, see
     * <a href="https://developers.facebook.com/docs/graph-api/reference/whats-app-business-account-to-number-current-status/register/"
     * WhatsApp Business Phone Number Register</a>.
     *
     * @param accessToken   The access token used to authenticate the API request.
     * @param phoneNumberId The ID of the phone number to be registered.
     * @throws IllegalStateException If the API returns an unsuccessful result.
     */
    public void registerPhoneNumber(String accessToken, String phoneNumberId) {
        FacebookClient client = new DefaultFacebookClient(accessToken, Version.LATEST);

        JsonObject request = new JsonObject();
        request.add("pin", "000000");
        request.add("messaging_product", "whatsapp");

        JsonObject response = client.publish(phoneNumberId + "/register", JsonObject.class, Body.withData(request));

        if (!response.getBoolean("success", false))
            throw new IllegalStateException("Failed to register phone number with id: " + phoneNumberId);
    }
}