import { IRelease } from 'app/shared/model//release.model';
import { IIssue } from 'app/shared/model//issue.model';
import { IVersion } from 'app/shared/model//version.model';

export interface IProject {
    id?: number;
    name?: string;
    key?: string;
    release?: IRelease;
    issues?: IIssue[];
    versions?: IVersion[];
}

export class Project implements IProject {
    constructor(
        public id?: number,
        public name?: string,
        public key?: string,
        public release?: IRelease,
        public issues?: IIssue[],
        public versions?: IVersion[]
    ) {}
}
