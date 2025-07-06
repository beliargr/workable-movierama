import {Routes} from '@angular/router';
import {MoviesComponent} from './movies/movies.component';
import {Empty} from './empty/empty';
import {authGuard} from "../auth/auth.guard";

export default [
    {path: 'movies', component: MoviesComponent, canActivate: [authGuard]},
    {path: 'empty', component: Empty},
    {path: '**', redirectTo: '/notfound'}
] as Routes;


