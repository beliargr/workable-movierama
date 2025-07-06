import { Component } from '@angular/core';
import {Router, RouterModule} from '@angular/router';
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {Toast, ToastModule} from "primeng/toast";
import {MessageModule} from "primeng/message";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterModule, TranslateModule, ToastModule],
    template: `<router-outlet></router-outlet>
    <p-toast position="top-right"></p-toast>`
})
export class AppComponent {

    constructor(public translate: TranslateService, private router: Router) {
        translate.addLangs(['en', 'el']);
        translate.setDefaultLang('en');
        const browserLang: string | undefined = translate.getBrowserLang() != null ? translate.getBrowserLang() : 'en';
        translate.use(browserLang != null ? browserLang : 'en');
    }

}
