import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Sprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';
import { SprintComponent } from './sprint.component';
import { SprintDetailComponent } from './sprint-detail.component';
import { SprintUpdateComponent } from './sprint-update.component';
import { SprintDeletePopupComponent } from './sprint-delete-dialog.component';
import { ISprint } from 'app/shared/model/sprint.model';

@Injectable({ providedIn: 'root' })
export class SprintResolve implements Resolve<ISprint> {
    constructor(private service: SprintService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((sprint: HttpResponse<Sprint>) => sprint.body));
        }
        return of(new Sprint());
    }
}

export const sprintRoute: Routes = [
    {
        path: 'sprint',
        component: SprintComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'releaseVitalsApp.sprint.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sprint/:id/view',
        component: SprintDetailComponent,
        resolve: {
            sprint: SprintResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.sprint.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sprint/new',
        component: SprintUpdateComponent,
        resolve: {
            sprint: SprintResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.sprint.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'sprint/:id/edit',
        component: SprintUpdateComponent,
        resolve: {
            sprint: SprintResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.sprint.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sprintPopupRoute: Routes = [
    {
        path: 'sprint/:id/delete',
        component: SprintDeletePopupComponent,
        resolve: {
            sprint: SprintResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.sprint.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
