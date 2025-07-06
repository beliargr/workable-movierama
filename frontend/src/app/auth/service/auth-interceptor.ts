import {
    HttpErrorResponse,
    HttpEvent,
    HttpHeaders,
    HttpInterceptorFn,
    HttpResponse,
    HttpStatusCode
} from "@angular/common/http";
import {inject} from "@angular/core";
import {NGXLogger} from "ngx-logger";
import {Router} from "@angular/router";
import {AuthService} from "./auth.service";
import {catchError, of, tap, throwError} from "rxjs";
import {environment} from "../../../environments/environment";
import {MessageService} from "primeng/api";
import {TranslateService} from "@ngx-translate/core";


export const authInterceptor: HttpInterceptorFn = (req, next) => {

    const logger = inject(NGXLogger);
    const router = inject(Router);
    const authService = inject(AuthService);
    const messageService: MessageService = inject(MessageService);
    const translateService: TranslateService = inject(TranslateService);

    const authHeaders = new HttpHeaders({
        'Authorization': `Bearer ${authService.keycloak.token}`,
    });

    if (req.url.indexOf(environment.baseUrl) != -1) {
        req = req.clone({
            headers: authHeaders,
        });
    }

    return next(req)
        .pipe(
            tap((evt: HttpEvent<any>) => {
                if (evt instanceof HttpResponse) {
                    return of(evt);
                }
                return of(evt);
            }),
            catchError(err => {
                logger.error(err);
                if (err instanceof HttpErrorResponse) {
                    const type = err.error?.type;
                    if (type === 'API-ERROR') {
                        messageService.add({
                            severity: 'warn',
                            summary: translateService.instant(`errors.${err.error.errorCode}`),
                            life: 3000
                        });
                    }
                    if (err.status === HttpStatusCode.Forbidden) {
                        authService.logout();
                        /*router.navigateByUrl('/auth/login');*/
                    } else if (err.status === HttpStatusCode.Unauthorized) {
                        authService.logout();
                        /*router.navigateByUrl('/auth/login');*/
                    } else if (err.status === HttpStatusCode.InternalServerError) {
                        logger.error(err);
                    } else {
                        logger.error('Oops, something unexpected happened!');
                    }
                }
                return throwError(err);
            }));

}
