import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReleaseVitalsSharedModule } from 'app/shared';
import {
    EpicComponent,
    EpicDetailComponent,
    EpicUpdateComponent,
    EpicDeletePopupComponent,
    EpicDeleteDialogComponent,
    epicRoute,
    epicPopupRoute
} from './';

const ENTITY_STATES = [...epicRoute, ...epicPopupRoute];

@NgModule({
    imports: [ReleaseVitalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [EpicComponent, EpicDetailComponent, EpicUpdateComponent, EpicDeleteDialogComponent, EpicDeletePopupComponent],
    entryComponents: [EpicComponent, EpicUpdateComponent, EpicDeleteDialogComponent, EpicDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReleaseVitalsEpicModule {}
