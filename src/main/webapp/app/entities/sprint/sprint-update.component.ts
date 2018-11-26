import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';

@Component({
    selector: 'jhi-sprint-update',
    templateUrl: './sprint-update.component.html'
})
export class SprintUpdateComponent implements OnInit {
    sprint: ISprint;
    isSaving: boolean;
    startDatetime: string;
    endDatetime: string;

    constructor(private sprintService: SprintService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ sprint }) => {
            this.sprint = sprint;
            this.startDatetime = this.sprint.startDatetime != null ? this.sprint.startDatetime.format(DATE_TIME_FORMAT) : null;
            this.endDatetime = this.sprint.endDatetime != null ? this.sprint.endDatetime.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.sprint.startDatetime = this.startDatetime != null ? moment(this.startDatetime, DATE_TIME_FORMAT) : null;
        this.sprint.endDatetime = this.endDatetime != null ? moment(this.endDatetime, DATE_TIME_FORMAT) : null;
        if (this.sprint.id !== undefined) {
            this.subscribeToSaveResponse(this.sprintService.update(this.sprint));
        } else {
            this.subscribeToSaveResponse(this.sprintService.create(this.sprint));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISprint>>) {
        result.subscribe((res: HttpResponse<ISprint>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
