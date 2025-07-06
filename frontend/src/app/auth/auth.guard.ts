import {CanActivateFn, Router} from "@angular/router";
import {AuthService} from "./service/auth.service";
import {inject} from "@angular/core";
import {NGXLogger} from "ngx-logger";

export const authGuard: CanActivateFn = () => {
    const authService: AuthService = inject(AuthService);
    const router: Router = inject(Router);
    const logger: NGXLogger = inject(NGXLogger);
    if (!authService.keycloak.token || authService.keycloak.isTokenExpired()) {
        logger.info(`Not Eligible to access protected route, logging out...`);
        authService.logout();
        return false;
    }
    logger.info(`Eligible to access protected route...`);
    return true;
}
