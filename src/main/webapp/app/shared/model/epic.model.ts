import { IIssue } from 'app/shared/model//issue.model';
import { IProject } from 'app/shared/model//project.model';

export interface IEpic {
    id?: number;
    name?: string;
    totalStoryPoints?: number;
    storyPointsCompleted?: number;
    remainingStoryPoints?: number;
    totalIssueCount?: number;
    percentageCompleted?: number;
    key?: string;
    epicBrowserURL?: string;
    unestimatedIssues?: IIssue[];
    projects?: IProject[];
}

export class Epic implements IEpic {
    constructor(
        public id?: number,
        public name?: string,
        public totalStoryPoints?: number,
        public storyPointsCompleted?: number,
        public remainingStoryPoints?: number,
        public totalIssueCount?: number,
        public percentageCompleted?: number,
        public key?: string,
        public epicBrowserURL?: string,
        public unestimatedIssues?: IIssue[],
        public projects?: IProject[]
    ) {}
}
