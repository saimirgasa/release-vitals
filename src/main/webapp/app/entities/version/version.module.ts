import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReleaseVitalsSharedModule } from 'app/shared';
import {
    VersionComponent,
    VersionDetailComponent,
    VersionUpdateComponent,
    VersionDeletePopupComponent,
    VersionDeleteDialogComponent,
    versionRoute,
    versionPopupRoute
} from './';

const ENTITY_STATES = [...versionRoute, ...versionPopupRoute];

@NgModule({
    imports: [ReleaseVitalsSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        VersionComponent,
        VersionDetailComponent,
        VersionUpdateComponent,
        VersionDeleteDialogComponent,
        VersionDeletePopupComponent
    ],
    entryComponents: [VersionComponent, VersionUpdateComponent, VersionDeleteDialogComponent, VersionDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReleaseVitalsVersionModule {}
