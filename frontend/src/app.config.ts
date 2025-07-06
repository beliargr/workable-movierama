import {HttpClient, provideHttpClient, withFetch, withInterceptors} from '@angular/common/http';
import {ApplicationConfig, importProvidersFrom, inject, provideAppInitializer} from '@angular/core';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {provideRouter, withEnabledBlockingInitialNavigation, withInMemoryScrolling} from '@angular/router';
import Aura from '@primeng/themes/aura';
import {providePrimeNG} from 'primeng/config';
import {appRoutes} from './app.routes';
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {LoggerModule, NgxLoggerLevel, TOKEN_LOGGER_SERVER_SERVICE} from "ngx-logger";
import {authInterceptor} from "./app/auth/service/auth-interceptor";
import {AuthService} from "./app/auth/service/auth.service";
import {MessageService} from "primeng/api";
import {CustomLoggerHttpService} from "./app/pages/service/NgxLoggerHttpService";

const httpLoaderFactory: (http: HttpClient) => TranslateHttpLoader = (http: HttpClient) =>
    new TranslateHttpLoader(http, './assets/i18n/', '.json');


export const appConfig: ApplicationConfig = {
    providers: [

        provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
        provideAnimationsAsync(),
        provideAppInitializer(async () => {
            const authService = inject(AuthService);
            await authService.init();
        }),
        providePrimeNG({theme: {preset: Aura, options: {darkModeSelector: '.app-dark'}}}),
        provideRouter(appRoutes, withInMemoryScrolling({
            anchorScrolling: 'enabled',
            scrollPositionRestoration: 'enabled'
        }), withEnabledBlockingInitialNavigation()),
        importProvidersFrom([TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: httpLoaderFactory,
                deps: [HttpClient],
            },
        }),
            LoggerModule.forRoot({
                serverLoggingUrl: 'movierama/api/log',
                level: NgxLoggerLevel.DEBUG,
                serverLogLevel: NgxLoggerLevel.INFO,
            },{
                serverProvider: {
                    provide: TOKEN_LOGGER_SERVER_SERVICE, useClass: CustomLoggerHttpService
                }
            }),]),
        MessageService
    ]
};
