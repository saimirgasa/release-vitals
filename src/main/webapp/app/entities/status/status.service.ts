import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IStatus } from 'app/shared/model/status.model';

type EntityResponseType = HttpResponse<IStatus>;
type EntityArrayResponseType = HttpResponse<IStatus[]>;

@Injectable({ providedIn: 'root' })
export class StatusService {
    private resourceUrl = SERVER_API_URL + 'api/statuses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/statuses';

    constructor(private http: HttpClient) {}

    create(status: IStatus): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(status);
        return this.http
            .post<IStatus>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(status: IStatus): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(status);
        return this.http
            .put<IStatus>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(status: IStatus): IStatus {
        const copy: IStatus = Object.assign({}, status, {
            timeChaged: status.timeChaged != null && status.timeChaged.isValid() ? status.timeChaged.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.timeChaged = res.body.timeChaged != null ? moment(res.body.timeChaged) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((status: IStatus) => {
            status.timeChaged = status.timeChaged != null ? moment(status.timeChaged) : null;
        });
        return res;
    }
}
