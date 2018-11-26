/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReleaseVitalsTestModule } from '../../../test.module';
import { ReleaseDeleteDialogComponent } from 'app/entities/release/release-delete-dialog.component';
import { ReleaseService } from 'app/entities/release/release.service';

describe('Component Tests', () => {
    describe('Release Management Delete Component', () => {
        let comp: ReleaseDeleteDialogComponent;
        let fixture: ComponentFixture<ReleaseDeleteDialogComponent>;
        let service: ReleaseService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReleaseVitalsTestModule],
                declarations: [ReleaseDeleteDialogComponent]
            })
                .overrideTemplate(ReleaseDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReleaseDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReleaseService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
