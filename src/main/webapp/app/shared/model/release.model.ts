import { Moment } from 'moment';
import { IProject } from 'app/shared/model//project.model';

export interface IRelease {
    id?: number;
    name?: string;
    startDate?: Moment;
    endDate?: Moment;
    projects?: IProject[];
}

export class Release implements IRelease {
    constructor(
        public id?: number,
        public name?: string,
        public startDate?: Moment,
        public endDate?: Moment,
        public projects?: IProject[]
    ) {}
}
