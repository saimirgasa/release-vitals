import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEpic } from 'app/shared/model/epic.model';

@Component({
    selector: 'jhi-epic-detail',
    templateUrl: './epic-detail.component.html'
})
export class EpicDetailComponent implements OnInit {
    epic: IEpic;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ epic }) => {
            this.epic = epic;
        });
    }

    previousState() {
        window.history.back();
    }
}
