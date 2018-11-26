import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEpic } from 'app/shared/model/epic.model';

type EntityResponseType = HttpResponse<IEpic>;
type EntityArrayResponseType = HttpResponse<IEpic[]>;

@Injectable({ providedIn: 'root' })
export class EpicService {
    private resourceUrl = SERVER_API_URL + 'api/epics';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/epics';

    constructor(private http: HttpClient) {}

    create(epic: IEpic): Observable<EntityResponseType> {
        return this.http.post<IEpic>(this.resourceUrl, epic, { observe: 'response' });
    }

    update(epic: IEpic): Observable<EntityResponseType> {
        return this.http.put<IEpic>(this.resourceUrl, epic, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IEpic>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEpic[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEpic[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
