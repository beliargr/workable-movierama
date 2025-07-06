import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {map} from "rxjs/operators";

export interface ILookup {
    label: string;
    value: any;
}


@Injectable()
export class LookupService {

    constructor(private http: HttpClient, private translateService: TranslateService) {}

    getLookups(): Observable<{ [key: string]: ILookup[] }> {
        return this.translateService.stream('LOOKUPS').pipe(
            map(translations => ({
                'MOVIE_GENRES': [
                    {label: translations?.MOVIE_GENRES?.ACTION, value: 'ACTION'},
                    {label: translations?.MOVIE_GENRES?.COMEDY, value: 'COMEDY'},
                    {label: translations?.MOVIE_GENRES?.DRAMA, value: 'DRAMA'},
                    {label: translations?.MOVIE_GENRES?.HORROR, value: 'HORROR'},
                    {label: translations?.MOVIE_GENRES?.ROMANCE, value: 'ROMANCE'},
                    {label: translations?.MOVIE_GENRES?.SCIENCE, value: 'SCIENCE'}
                ],
            })));
    }
}
