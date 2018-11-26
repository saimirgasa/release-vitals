import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRelease } from 'app/shared/model/release.model';
import { ReleaseService } from './release.service';

@Component({
    selector: 'jhi-release-delete-dialog',
    templateUrl: './release-delete-dialog.component.html'
})
export class ReleaseDeleteDialogComponent {
    release: IRelease;

    constructor(private releaseService: ReleaseService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.releaseService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'releaseListModification',
                content: 'Deleted an release'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-release-delete-popup',
    template: ''
})
export class ReleaseDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ release }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReleaseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.release = release;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
