import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import { Observable } from 'rxjs';

export interface IMovie {
    id?: number,
    title?: string,
    description?: string,
    genre?: string,
    createdBy?: string,
    createdDate?: string,
    lastModifiedDate?: string;
}

export interface IMoviesWithRatings {
    id?: number,
    title?: string,
    description?: string,
    genre?: string,
    createdBy?: string,
    createdDate?: string,
    lastModifiedDate?: string;
    likeCount?: number;
    likeHate?: number;
    userRating?: string;
    ratingId?: number;
}

@Injectable()
export class MovieService {

    serviceUrl: string = `${environment.baseUrl}/movies`;

    constructor(private http: HttpClient) {}

    getMovies(): Observable<IMovie[]> {
        return this.http.get<IMovie[]>(this.serviceUrl);
    }

    getMovie(id: number): Observable<IMovie> {
        const url: string = `${this.serviceUrl}/${id}`;
        return this.http.get<IMovie>(url);
    }

    getMoviesWithRatings(): Observable<IMoviesWithRatings[]> {
        const url: string = `${this.serviceUrl}/ratings`;
        return this.http.get<IMoviesWithRatings[]>(url);
    }

    delete(id: number): Observable<any> {
        const url: string = `${this.serviceUrl}/${id}`;
        return this.http.delete(url);
    }

    bulkDelete(ids: number[]): Observable<any> {
        const url: string = `${this.serviceUrl}/bulk`;
        return this.http.delete(url, {body: {ids: ids}});
    }

    save(record: IMovie): Observable<IMovie> {
        if (record.id)
            return this.http.patch<IMovie>(`${this.serviceUrl}/${record.id}`, record);
        else
            return this.http.post<IMovie>(this.serviceUrl, record);
    }

}
