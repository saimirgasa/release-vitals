/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReleaseVitalsTestModule } from '../../../test.module';
import { EpicUpdateComponent } from 'app/entities/epic/epic-update.component';
import { EpicService } from 'app/entities/epic/epic.service';
import { Epic } from 'app/shared/model/epic.model';

describe('Component Tests', () => {
    describe('Epic Management Update Component', () => {
        let comp: EpicUpdateComponent;
        let fixture: ComponentFixture<EpicUpdateComponent>;
        let service: EpicService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReleaseVitalsTestModule],
                declarations: [EpicUpdateComponent]
            })
                .overrideTemplate(EpicUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EpicUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EpicService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Epic(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.epic = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Epic();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.epic = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
