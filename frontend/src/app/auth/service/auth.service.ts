import {Injectable} from "@angular/core";
import {NGXLogger} from "ngx-logger";
import Keycloak from "keycloak-js";
import {environment} from "../../../environments/environment";

export interface IUserProfile {
    username?: string;
    email?: string;
    firstName?: string;
    lastName?: string;
    token?: string;
    roles?: string[];
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private _profile: IUserProfile;
    get profile() {
        return this._profile;
    }

    private _keycloak: Keycloak | undefined;
    get keycloak() {
        if (!this._keycloak) {
            this._keycloak = new Keycloak({
                url: environment.oAuth2Conf.issuer,
                clientId: environment.oAuth2Conf.clientId,
                realm: environment.oAuth2Conf.realm
            });
        }
        return this._keycloak;
    }

    constructor(/*private logger: NGXLogger*/) {}

    async init(): Promise<void> {
        //this.logger.info(`Authenticating user...`);
        const isAuthenticated: boolean = await this.keycloak.init({
            onLoad: 'login-required',
            redirectUri: environment.oAuth2Conf.redirectUrl,
            scope: environment.oAuth2Conf.scope,
        });

        if (isAuthenticated) {
            //this.logger.info(`User authenticated, redirecting to homepage`);
            if (isAuthenticated) {
                this._profile = (await this.keycloak.loadUserProfile()) as IUserProfile;
                this._profile.token = this.keycloak.token || '';
                this._profile.roles = this.keycloak.tokenParsed?.resource_access?.[environment.oAuth2Conf.clientId]?.roles || [];
            }
        }
        return;
    }

    login() {
        return this.keycloak.login();
    }

    logout() {
        //this.logger.info(`Logging out user...`);
        return this.keycloak?.logout({redirectUri: environment.oAuth2Conf.logoutUrl});
    }

}
