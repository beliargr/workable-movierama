import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import { Observable } from 'rxjs';

export interface IRating {
    id?: number,
    movieId?: number,
    type?: IRatingType;
}

export enum IRatingType {
    LIKE=("LIKE"),
    HATE=("HATE")
}

@Injectable()
export class RatingService {

    serviceUrl: string = `${environment.baseUrl}/ratings`;

    constructor(private http: HttpClient) {}

    getRatings(): Observable<IRating[]> {
        return this.http.get<IRating[]>(this.serviceUrl);
    }

    getRating(id: number): Observable<IRating> {
        const url: string = `${this.serviceUrl}/${id}`;
        return this.http.get<IRating>(url);
    }

    delete(id: number): Observable<any> {
        const url: string = `${this.serviceUrl}/${id}`;
        return this.http.delete(url);
    }

    save(record: IRating): Observable<IRating> {
        if (record.id)
            return this.http.patch<IRating>(`${this.serviceUrl}/${record.id}`, record);
        else
            return this.http.post<IRating>(this.serviceUrl, record);
    }

    rate(record: IRating): Observable<IRating> {
        const url: string = `${this.serviceUrl}/rate-movie/${record.movieId}`;
        return this.http.post<IRating>(`${url}`, record);
    }

}
