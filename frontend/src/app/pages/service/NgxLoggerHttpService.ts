import {Injectable, NgZone} from '@angular/core';
import {HttpBackend, HttpRequest} from '@angular/common/http';
import {NGXLoggerServerService} from "ngx-logger";
import {AuthService} from "../../auth/service/auth.service";

@Injectable()
export class CustomLoggerHttpService extends NGXLoggerServerService {

    constructor(httpBackend: HttpBackend, ngZone: NgZone, private authService: AuthService) {
        super(httpBackend, ngZone);
    }

    protected override alterHttpRequest(httpRequest: HttpRequest<any>): HttpRequest<any> {
        // Alter httpRequest by adding auth token to header
        httpRequest = httpRequest.clone({
            setHeaders: {
                ['Authorization']: `Bearer ${this.authService.profile.token}`,
            },
        });
        return httpRequest;
    }
}
