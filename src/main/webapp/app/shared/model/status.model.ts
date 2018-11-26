import { Moment } from 'moment';
import { IIssue } from 'app/shared/model//issue.model';

export interface IStatus {
    id?: number;
    fromStatus?: string;
    toStatus?: string;
    timeChaged?: Moment;
    issue?: IIssue;
}

export class Status implements IStatus {
    constructor(
        public id?: number,
        public fromStatus?: string,
        public toStatus?: string,
        public timeChaged?: Moment,
        public issue?: IIssue
    ) {}
}
