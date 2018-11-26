import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReleaseVitalsSharedModule } from 'app/shared';
import {
    IssueComponent,
    IssueDetailComponent,
    IssueUpdateComponent,
    IssueDeletePopupComponent,
    IssueDeleteDialogComponent,
    issueRoute,
    issuePopupRoute
} from './';

const ENTITY_STATES = [...issueRoute, ...issuePopupRoute];

@NgModule({
    imports: [ReleaseVitalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [IssueComponent, IssueDetailComponent, IssueUpdateComponent, IssueDeleteDialogComponent, IssueDeletePopupComponent],
    entryComponents: [IssueComponent, IssueUpdateComponent, IssueDeleteDialogComponent, IssueDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReleaseVitalsIssueModule {}
