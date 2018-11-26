/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SprintService } from 'app/entities/sprint/sprint.service';
import { ISprint, Sprint } from 'app/shared/model/sprint.model';

describe('Service Tests', () => {
    describe('Sprint Service', () => {
        let injector: TestBed;
        let service: SprintService;
        let httpMock: HttpTestingController;
        let elemDefault: ISprint;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(SprintService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Sprint(0, 'AAAAAAA', currentDate, currentDate, 0);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        startDatetime: currentDate.format(DATE_TIME_FORMAT),
                        endDatetime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Sprint', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        startDatetime: currentDate.format(DATE_TIME_FORMAT),
                        endDatetime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        startDatetime: currentDate,
                        endDatetime: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Sprint(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Sprint', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        startDatetime: currentDate.format(DATE_TIME_FORMAT),
                        endDatetime: currentDate.format(DATE_TIME_FORMAT),
                        velocity: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        startDatetime: currentDate,
                        endDatetime: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Sprint', async () => {
                const returnedFromService = Object.assign(
                    {
                        name: 'BBBBBB',
                        startDatetime: currentDate.format(DATE_TIME_FORMAT),
                        endDatetime: currentDate.format(DATE_TIME_FORMAT),
                        velocity: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        startDatetime: currentDate,
                        endDatetime: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(take(1), map(resp => resp.body))
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Sprint', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
