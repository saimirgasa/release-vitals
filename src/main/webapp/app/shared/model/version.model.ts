import { IIssue } from 'app/shared/model//issue.model';

export interface IVersion {
    id?: number;
    name?: string;
    issue?: IIssue;
}

export class Version implements IVersion {
    constructor(public id?: number, public name?: string, public issue?: IIssue) {}
}
