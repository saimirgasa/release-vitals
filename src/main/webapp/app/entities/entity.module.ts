import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ReleaseVitalsReleaseModule } from './release/release.module';
import { ReleaseVitalsSprintModule } from './sprint/sprint.module';
import { ReleaseVitalsProjectModule } from './project/project.module';
import { ReleaseVitalsVersionModule } from './version/version.module';
import { ReleaseVitalsEpicModule } from './epic/epic.module';
import { ReleaseVitalsIssueModule } from './issue/issue.module';
import { ReleaseVitalsStatusModule } from './status/status.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ReleaseVitalsReleaseModule,
        ReleaseVitalsSprintModule,
        ReleaseVitalsProjectModule,
        ReleaseVitalsVersionModule,
        ReleaseVitalsEpicModule,
        ReleaseVitalsIssueModule,
        ReleaseVitalsStatusModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReleaseVitalsEntityModule {}
