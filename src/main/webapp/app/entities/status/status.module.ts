import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReleaseVitalsSharedModule } from 'app/shared';
import {
    StatusComponent,
    StatusDetailComponent,
    StatusUpdateComponent,
    StatusDeletePopupComponent,
    StatusDeleteDialogComponent,
    statusRoute,
    statusPopupRoute
} from './';

const ENTITY_STATES = [...statusRoute, ...statusPopupRoute];

@NgModule({
    imports: [ReleaseVitalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [StatusComponent, StatusDetailComponent, StatusUpdateComponent, StatusDeleteDialogComponent, StatusDeletePopupComponent],
    entryComponents: [StatusComponent, StatusUpdateComponent, StatusDeleteDialogComponent, StatusDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReleaseVitalsStatusModule {}
