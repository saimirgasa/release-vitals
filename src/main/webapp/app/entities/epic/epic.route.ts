import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Epic } from 'app/shared/model/epic.model';
import { EpicService } from './epic.service';
import { EpicComponent } from './epic.component';
import { EpicDetailComponent } from './epic-detail.component';
import { EpicUpdateComponent } from './epic-update.component';
import { EpicDeletePopupComponent } from './epic-delete-dialog.component';
import { IEpic } from 'app/shared/model/epic.model';

@Injectable({ providedIn: 'root' })
export class EpicResolve implements Resolve<IEpic> {
    constructor(private service: EpicService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((epic: HttpResponse<Epic>) => epic.body));
        }
        return of(new Epic());
    }
}

export const epicRoute: Routes = [
    {
        path: 'epic',
        component: EpicComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'epic/:id/view',
        component: EpicDetailComponent,
        resolve: {
            epic: EpicResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'epic/new',
        component: EpicUpdateComponent,
        resolve: {
            epic: EpicResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'epic/:id/edit',
        component: EpicUpdateComponent,
        resolve: {
            epic: EpicResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const epicPopupRoute: Routes = [
    {
        path: 'epic/:id/delete',
        component: EpicDeletePopupComponent,
        resolve: {
            epic: EpicResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.epic.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
