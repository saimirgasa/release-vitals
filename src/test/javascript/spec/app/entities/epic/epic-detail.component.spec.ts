/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReleaseVitalsTestModule } from '../../../test.module';
import { EpicDetailComponent } from 'app/entities/epic/epic-detail.component';
import { Epic } from 'app/shared/model/epic.model';

describe('Component Tests', () => {
    describe('Epic Management Detail Component', () => {
        let comp: EpicDetailComponent;
        let fixture: ComponentFixture<EpicDetailComponent>;
        const route = ({ data: of({ epic: new Epic(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReleaseVitalsTestModule],
                declarations: [EpicDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(EpicDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EpicDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.epic).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
