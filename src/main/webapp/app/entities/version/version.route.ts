import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Version } from 'app/shared/model/version.model';
import { VersionService } from './version.service';
import { VersionComponent } from './version.component';
import { VersionDetailComponent } from './version-detail.component';
import { VersionUpdateComponent } from './version-update.component';
import { VersionDeletePopupComponent } from './version-delete-dialog.component';
import { IVersion } from 'app/shared/model/version.model';

@Injectable({ providedIn: 'root' })
export class VersionResolve implements Resolve<IVersion> {
    constructor(private service: VersionService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((version: HttpResponse<Version>) => version.body));
        }
        return of(new Version());
    }
}

export const versionRoute: Routes = [
    {
        path: 'version',
        component: VersionComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'releaseVitalsApp.version.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version/:id/view',
        component: VersionDetailComponent,
        resolve: {
            version: VersionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.version.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version/new',
        component: VersionUpdateComponent,
        resolve: {
            version: VersionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.version.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'version/:id/edit',
        component: VersionUpdateComponent,
        resolve: {
            version: VersionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.version.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const versionPopupRoute: Routes = [
    {
        path: 'version/:id/delete',
        component: VersionDeletePopupComponent,
        resolve: {
            version: VersionResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'releaseVitalsApp.version.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
