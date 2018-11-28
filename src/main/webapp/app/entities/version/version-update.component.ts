import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IVersion } from 'app/shared/model/version.model';
import { VersionService } from './version.service';
import { IIssue } from 'app/shared/model/issue.model';
import { IssueService } from 'app/entities/issue';

@Component({
    selector: 'jhi-version-update',
    templateUrl: './version-update.component.html'
})
export class VersionUpdateComponent implements OnInit {
    version: IVersion;
    isSaving: boolean;

    issues: IIssue[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private versionService: VersionService,
        private issueService: IssueService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ version }) => {
            this.version = version;
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
        if (this.version.id !== undefined) {
            this.subscribeToSaveResponse(this.versionService.update(this.version));
        } else {
            this.subscribeToSaveResponse(this.versionService.create(this.version));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IVersion>>) {
        result.subscribe((res: HttpResponse<IVersion>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
