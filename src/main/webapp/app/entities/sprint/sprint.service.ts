import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISprint } from 'app/shared/model/sprint.model';

type EntityResponseType = HttpResponse<ISprint>;
type EntityArrayResponseType = HttpResponse<ISprint[]>;

@Injectable({ providedIn: 'root' })
export class SprintService {
    private resourceUrl = SERVER_API_URL + 'api/sprints';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/sprints';

    constructor(private http: HttpClient) {}

    create(sprint: ISprint): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sprint);
        return this.http
            .post<ISprint>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(sprint: ISprint): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(sprint);
        return this.http
            .put<ISprint>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISprint>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISprint[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISprint[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(sprint: ISprint): ISprint {
        const copy: ISprint = Object.assign({}, sprint, {
            startDatetime: sprint.startDatetime != null && sprint.startDatetime.isValid() ? sprint.startDatetime.toJSON() : null,
            endDatetime: sprint.endDatetime != null && sprint.endDatetime.isValid() ? sprint.endDatetime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.startDatetime = res.body.startDatetime != null ? moment(res.body.startDatetime) : null;
        res.body.endDatetime = res.body.endDatetime != null ? moment(res.body.endDatetime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((sprint: ISprint) => {
            sprint.startDatetime = sprint.startDatetime != null ? moment(sprint.startDatetime) : null;
            sprint.endDatetime = sprint.endDatetime != null ? moment(sprint.endDatetime) : null;
        });
        return res;
    }
}
