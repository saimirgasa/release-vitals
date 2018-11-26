import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEpic } from 'app/shared/model/epic.model';
import { EpicService } from './epic.service';

@Component({
    selector: 'jhi-epic-delete-dialog',
    templateUrl: './epic-delete-dialog.component.html'
})
export class EpicDeleteDialogComponent {
    epic: IEpic;

    constructor(private epicService: EpicService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.epicService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'epicListModification',
                content: 'Deleted an epic'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-epic-delete-popup',
    template: ''
})
export class EpicDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ epic }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(EpicDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.epic = epic;
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
