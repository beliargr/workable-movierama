import {Routes} from '@angular/router';
import {Access} from './access';
import {Login} from './login';
import {Error} from './error';
import {SsoComponent} from "./sso.component";

export default [
    {path: 'access', component: Access},
    {path: 'error', component: Error},
    {path: 'login', component: Login},
    {path: 'login-callback', component: SsoComponent},
] as Routes;
