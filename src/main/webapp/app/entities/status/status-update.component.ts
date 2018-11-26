import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from './status.service';
import { IIssue } from 'app/shared/model/issue.model';
import { IssueService } from 'app/entities/issue';

@Component({
    selector: 'jhi-status-update',
    templateUrl: './status-update.component.html'
})
export class StatusUpdateComponent implements OnInit {
    status: IStatus;
    isSaving: boolean;

    issues: IIssue[];
    timeChaged: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private statusService: StatusService,
        private issueService: IssueService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ status }) => {
            this.status = status;
            this.timeChaged = this.status.timeChaged != null ? this.status.timeChaged.format(DATE_TIME_FORMAT) : null;
        });
        this.issueService.query().subscribe(
            (res: HttpResponse<IIssue[]>) => {
                this.issues = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.status.timeChaged = this.timeChaged != null ? moment(this.timeChaged, DATE_TIME_FORMAT) : null;
        if (this.status.id !== undefined) {
            this.subscribeToSaveResponse(this.statusService.update(this.status));
        } else {
            this.subscribeToSaveResponse(this.statusService.create(this.status));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IStatus>>) {
        result.subscribe((res: HttpResponse<IStatus>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackIssueById(index: number, item: IIssue) {
        return item.id;
    }
}
