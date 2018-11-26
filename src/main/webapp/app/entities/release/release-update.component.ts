import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRelease } from 'app/shared/model/release.model';
import { ReleaseService } from './release.service';

@Component({
    selector: 'jhi-release-update',
    templateUrl: './release-update.component.html'
})
export class ReleaseUpdateComponent implements OnInit {
    release: IRelease;
    isSaving: boolean;
    startDate: string;
    endDate: string;

    constructor(private releaseService: ReleaseService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ release }) => {
            this.release = release;
            this.startDate = this.release.startDate != null ? this.release.startDate.format(DATE_TIME_FORMAT) : null;
            this.endDate = this.release.endDate != null ? this.release.endDate.format(DATE_TIME_FORMAT) : null;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.release.startDate = this.startDate != null ? moment(this.startDate, DATE_TIME_FORMAT) : null;
        this.release.endDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;
        if (this.release.id !== undefined) {
            this.subscribeToSaveResponse(this.releaseService.update(this.release));
        } else {
            this.subscribeToSaveResponse(this.releaseService.create(this.release));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRelease>>) {
        result.subscribe((res: HttpResponse<IRelease>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
