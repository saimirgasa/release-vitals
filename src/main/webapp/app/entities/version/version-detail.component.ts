import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVersion } from 'app/shared/model/version.model';

@Component({
    selector: 'jhi-version-detail',
    templateUrl: './version-detail.component.html'
})
export class VersionDetailComponent implements OnInit {
    version: IVersion;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ version }) => {
            this.version = version;
        });
    }

    previousState() {
        window.history.back();
    }
}
