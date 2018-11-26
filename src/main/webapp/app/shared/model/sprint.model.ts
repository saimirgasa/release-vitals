import { Moment } from 'moment';
import { IIssue } from 'app/shared/model//issue.model';

export interface ISprint {
    id?: number;
    name?: string;
    startDatetime?: Moment;
    endDatetime?: Moment;
    velocity?: number;
    issues?: IIssue[];
}

export class Sprint implements ISprint {
    constructor(
        public id?: number,
        public name?: string,
        public startDatetime?: Moment,
        public endDatetime?: Moment,
        public velocity?: number,
        public issues?: IIssue[]
    ) {}
}
