import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, Subject, merge } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime, distinctUntilChanged, filter, map } from 'rxjs/operators';

import { IVersion } from 'app/shared/model/version.model';
import { VersionService } from './version.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';
import { IIssue } from 'app/shared/model/issue.model';
import { IssueService } from 'app/entities/issue';

// @Component({
//     selector: 'jhi-version-update',
//     templateUrl: './version-update.component.html'
// })
@Component({
    selector: 'ngbd-typeahead-focus',
    templateUrl: './version-update.component.html'
})
export class VersionUpdateComponent implements OnInit {
    version: IVersion;
    isSaving: boolean;

    projects: IProject[];

    issues: IIssue[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private versionService: VersionService,
        private projectService: ProjectService,
        private issueService: IssueService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ version }) => {
            this.version = version;
        });
        this.projectService.query().subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
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

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }

    trackIssueById(index: number, item: IIssue) {
        return item.id;
    }

    @ViewChild('instance') instance: NgbTypeahead;
    focus$ = new Subject<string>();
    click$ = new Subject<string>();

    search = (text$: Observable<string>) => {
        const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());
        const clicksWithClosedPopup$ = this.click$.pipe(filter(() => !this.instance.isPopupOpen()));
        const inputFocus$ = this.focus$;

        return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
            map(term =>
                (term === '' ? this.projects : this.projects.filter(v => v.name.toLowerCase().indexOf(term.toLowerCase()) > -1)).slice(
                    0,
                    10
                )
            )
        );
    };
}
