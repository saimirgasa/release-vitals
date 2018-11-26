import { IRelease } from 'app/shared/model//release.model';
import { IVersion } from 'app/shared/model//version.model';
import { IEpic } from 'app/shared/model//epic.model';
import { IIssue } from 'app/shared/model//issue.model';

export interface IProject {
    id?: number;
    name?: string;
    key?: string;
    release?: IRelease;
    versions?: IVersion[];
    epics?: IEpic[];
    issues?: IIssue[];
}

export class Project implements IProject {
    constructor(
        public id?: number,
        public name?: string,
        public key?: string,
        public release?: IRelease,
        public versions?: IVersion[],
        public epics?: IEpic[],
        public issues?: IIssue[]
    ) {}
}
