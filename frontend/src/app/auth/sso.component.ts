import {Component} from "@angular/core";
import {Router, RouterModule} from "@angular/router";
import {NGXLogger} from "ngx-logger";

@Component({
    template: ``,
    standalone: true,
    imports: [RouterModule],
    providers: [NGXLogger]
})
export class SsoComponent {
    constructor(private logger: NGXLogger, private router: Router) {
        this.logger.info('Redirected after successfully loging in...');
        this.router.navigate(['/pages/movies']);
    }
}
