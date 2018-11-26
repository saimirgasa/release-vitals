/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReleaseVitalsTestModule } from '../../../test.module';
import { ReleaseDetailComponent } from 'app/entities/release/release-detail.component';
import { Release } from 'app/shared/model/release.model';

describe('Component Tests', () => {
    describe('Release Management Detail Component', () => {
        let comp: ReleaseDetailComponent;
        let fixture: ComponentFixture<ReleaseDetailComponent>;
        const route = ({ data: of({ release: new Release(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReleaseVitalsTestModule],
                declarations: [ReleaseDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReleaseDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReleaseDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.release).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
