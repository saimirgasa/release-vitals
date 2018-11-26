import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRelease } from 'app/shared/model/release.model';

type EntityResponseType = HttpResponse<IRelease>;
type EntityArrayResponseType = HttpResponse<IRelease[]>;

@Injectable({ providedIn: 'root' })
export class ReleaseService {
    private resourceUrl = SERVER_API_URL + 'api/releases';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/releases';

    constructor(private http: HttpClient) {}

    create(release: IRelease): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(release);
        return this.http
            .post<IRelease>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(release: IRelease): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(release);
        return this.http
            .put<IRelease>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IRelease>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRelease[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IRelease[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(release: IRelease): IRelease {
        const copy: IRelease = Object.assign({}, release, {
            startDate: release.startDate != null && release.startDate.isValid() ? release.startDate.toJSON() : null,
            endDate: release.endDate != null && release.endDate.isValid() ? release.endDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startDate = res.body.startDate != null ? moment(res.body.startDate) : null;
        res.body.endDate = res.body.endDate != null ? moment(res.body.endDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((release: IRelease) => {
            release.startDate = release.startDate != null ? moment(release.startDate) : null;
            release.endDate = release.endDate != null ? moment(release.endDate) : null;
        });
        return res;
    }
}
