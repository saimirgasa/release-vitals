import { ISprint } from 'app/shared/model//sprint.model';
import { IProject } from 'app/shared/model//project.model';
import { IEpic } from 'app/shared/model//epic.model';
import { IStatus } from 'app/shared/model//status.model';
import { IVersion } from 'app/shared/model//version.model';

export interface IIssue {
    id?: number;
    title?: string;
    description?: string;
    key?: string;
    sprint?: ISprint;
    project?: IProject;
    epic?: IEpic;
    statuses?: IStatus[];
    fixVersions?: IVersion[];
}

export class Issue implements IIssue {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public key?: string,
        public sprint?: ISprint,
        public project?: IProject,
        public epic?: IEpic,
        public statuses?: IStatus[],
        public fixVersions?: IVersion[]
    ) {}
}
