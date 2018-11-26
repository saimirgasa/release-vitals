import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IIssue } from 'app/shared/model/issue.model';
import { IssueService } from './issue.service';
import { ISprint } from 'app/shared/model/sprint.model';
import { SprintService } from 'app/entities/sprint';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';
import { IEpic } from 'app/shared/model/epic.model';
import { EpicService } from 'app/entities/epic';

@Component({
    selector: 'jhi-issue-update',
    templateUrl: './issue-update.component.html'
})
export class IssueUpdateComponent implements OnInit {
    issue: IIssue;
    isSaving: boolean;

    sprints: ISprint[];

    projects: IProject[];

    epics: IEpic[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private issueService: IssueService,
        private sprintService: SprintService,
        private projectService: ProjectService,
        private epicService: EpicService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ issue }) => {
            this.issue = issue;
        });
        this.sprintService.query().subscribe(
            (res: HttpResponse<ISprint[]>) => {
                this.sprints = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.projectService.query().subscribe(
            (res: HttpResponse<IProject[]>) => {
                this.projects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.epicService.query().subscribe(
            (res: HttpResponse<IEpic[]>) => {
                this.epics = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.issue.id !== undefined) {
            this.subscribeToSaveResponse(this.issueService.update(this.issue));
        } else {
            this.subscribeToSaveResponse(this.issueService.create(this.issue));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IIssue>>) {
        result.subscribe((res: HttpResponse<IIssue>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSprintById(index: number, item: ISprint) {
        return item.id;
    }

    trackProjectById(index: number, item: IProject) {
        return item.id;
    }

    trackEpicById(index: number, item: IEpic) {
        return item.id;
    }
}
