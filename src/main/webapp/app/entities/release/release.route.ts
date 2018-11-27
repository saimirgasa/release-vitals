import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Release } from 'app/shared/model/release.model';
import { ReleaseService } from './release.service';
import { ReleaseComponent } from './release.component';
import { ReleaseDetailComponent } from './release-detail.component';
import { ReleaseUpdateComponent } from './release-update.component';
import { ReleaseDeletePopupComponent } from './release-delete-dialog.component';
import { IRelease } from 'app/shared/model/release.model';

@Injectable({ providedIn: 'root' })
export class ReleaseResolve implements Resolve<IRelease> {
    constructor(private service: ReleaseService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((release: HttpResponse<Release>) => release.body));
        }
        return of(new Release());
    }
}

export const releaseRoute: Routes = [
    {
        path: 'release',
        component: ReleaseComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.release.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'release/:id/view',
        component: ReleaseDetailComponent,
        resolve: {
            release: ReleaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.release.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'release/new',
        component: ReleaseUpdateComponent,
        resolve: {
            release: ReleaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.release.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'release/:id/edit',
        component: ReleaseUpdateComponent,
        resolve: {
            release: ReleaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.release.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const releasePopupRoute: Routes = [
    {
        path: 'release/:id/delete',
        component: ReleaseDeletePopupComponent,
        resolve: {
            release: ReleaseResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.release.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
