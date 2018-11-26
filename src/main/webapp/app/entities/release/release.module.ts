import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReleaseVitalsSharedModule } from 'app/shared';
import {
    ReleaseComponent,
    ReleaseDetailComponent,
    ReleaseUpdateComponent,
    ReleaseDeletePopupComponent,
    ReleaseDeleteDialogComponent,
    releaseRoute,
    releasePopupRoute
} from './';

const ENTITY_STATES = [...releaseRoute, ...releasePopupRoute];

@NgModule({
    imports: [ReleaseVitalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReleaseComponent,
        ReleaseDetailComponent,
        ReleaseUpdateComponent,
        ReleaseDeleteDialogComponent,
        ReleaseDeletePopupComponent
    ],
    entryComponents: [ReleaseComponent, ReleaseUpdateComponent, ReleaseDeleteDialogComponent, ReleaseDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReleaseVitalsReleaseModule {}
