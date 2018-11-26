import { IProject } from 'app/shared/model//project.model';
import { IIssue } from 'app/shared/model//issue.model';

export interface IVersion {
    id?: number;
    name?: string;
    project?: IProject;
    issue?: IIssue;
}

export class Version implements IVersion {
    constructor(public id?: number, public name?: string, public project?: IProject, public issue?: IIssue) {}
}
