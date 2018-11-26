import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRelease } from 'app/shared/model/release.model';

@Component({
    selector: 'jhi-release-detail',
    templateUrl: './release-detail.component.html'
})
export class ReleaseDetailComponent implements OnInit {
    release: IRelease;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ release }) => {
            this.release = release;
        });
    }

    previousState() {
        window.history.back();
    }
}
