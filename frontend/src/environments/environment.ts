const baseAuthServerUrl: string= "http://localhost:4200";
export const environment = {
    baseUrl: "movierama/api",
    oAuth2Conf: {
        issuer: baseAuthServerUrl,
        realm: 'movierama',
        clientId: "movierama-client",
        redirectUrl: 'http://localhost:4200/auth/login-callback',
        loginUrl: `${baseAuthServerUrl}/realms/movierama/protocol/openid-connect`,
        responseType: "code",
        scope: "openid profile email",
        showDebugInformation: true,
        logoutUrl: 'http://localhost:4200'
    }
}


