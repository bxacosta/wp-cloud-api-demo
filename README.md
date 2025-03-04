## WhatsApp Business Cloud API Demo

This repository is a demo of how to integrate
the [WhatsApp Business Cloud API](https://developers.facebook.com/docs/whatsapp/cloud-api)
through [Facebook's Graph API](https://developers.facebook.com/docs/graph-api/).
using Spring Boot.

## Features

- Uses [**restfb**](https://github.com/restfb/restfb) as
  the [Facebook's Graph API](https://developers.facebook.com/docs/graph-api/) client.
- Implements [**Embedded Signup**](https://developers.facebook.com/docs/whatsapp/embedded-signup) for authentication.
- Lists associated **WhatsApp Business Accounts**.
- Lists associated **WhatsApp Business Phone Numbers**.
- Lists associated **WhatsApp Message Templates**.

## Technologies Used

- Spring Boot 3.4
- Java 21
- Gradle
- lombok

## Requirements

- A Meta [Business Account](https://business.facebook.com/).
- A [Business App](https://developers.facebook.com/docs/development/create-an-app/app-dashboard/app-types#business),
  associated with your Meta [Business Account](https://business.facebook.com/).
- Add the [Facebook Login for Business](https://developers.facebook.com/docs/facebook-login/facebook-login-for-business)
  Product to your app.
- Create
  a [Facebook Login for Business configuration](https://developers.facebook.com/docs/facebook-login/facebook-login-for-business/#create-a-configuration)
  with the [whatsapp_business_management](https://developers.facebook.com/docs/permissions#w),
  [whatsapp_business_messaging](https://developers.facebook.com/docs/permissions#w)
  permission.
- The server where you will be hosting Embedded Signup must have a **valid SSL certificate** (https).

Check the official documentation for more details:

- [Embedded Signup Implementation](https://developers.facebook.com/docs/whatsapp/embedded-signup/implementation)
- [WhatsApp Business Management API](https://developers.facebook.com/docs/whatsapp/business-management-api/get-started)
- [WhatsApp Business Platform Cloud API](https://developers.facebook.com/docs/whatsapp/cloud-api/overview)

## Setup

### Facebook App Configuration

1. Set
   up [OAuth redirect URLs](https://developers.facebook.com/docs/whatsapp/embedded-signup/implementation#step-1--add-allowed-domains)
   to include the `/callback` endpoint.
2. Note your App ID, Config ID, and App Secret,
   check [Basic Settings](https://developers.facebook.com/docs/development/create-an-app/app-dashboard/basic-settings/).

### Configure `application.yml`

Update the configuration file `src/main/resources/application.yml` with your Facebook API credentials:

```yaml
facebook:
  appId: your_app_id
  configId: your_config_id
  appSecret: your_app_secret
  callbackUrl: https://your-app.com/callback
  webhookToken: wp-notification
```

Make sure to replace the placeholder values with your actual Facebook application credentials.

## Building and Running

- Build the Application

```bash
./gradlew build
```

-  Run the Application

```bash
./gradlew bootRun
```

Use `.\gradlew.bat` instead if you are on Windows.

## Usage

1. Navigate to `/login` to initiate the Facebook authentication flow.
2. After successful authentication, you will be redirected back to the application `/`.
3. The access token will be stored in the session.

## API Endpoints

### Authentication (Embedded Signup)

| Method | Endpoint    | Description                                    |
|--------|-------------|------------------------------------------------|
| `GET`  | `/login`    | Redirects the user to Facebook authentication. |
| `GET`  | `/callback` | Processes the authentication response.         |

### WhatsApp Business Data

| Method | Endpoint | Description                                                                        |
|--------|----------|------------------------------------------------------------------------------------|
| `GET`  | `/`      | Lists associated WhatsApp Business accounts, phone numbers, and message templates. |

## License

[MIT License](LICENSE)
