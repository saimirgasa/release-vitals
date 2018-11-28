/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { VersionComponentsPage, VersionDeleteDialog, VersionUpdatePage } from './version.page-object';

const expect = chai.expect;

describe('Version e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let versionUpdatePage: VersionUpdatePage;
    let versionComponentsPage: VersionComponentsPage;
    let versionDeleteDialog: VersionDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Versions', async () => {
        await navBarPage.goToEntity('version');
        versionComponentsPage = new VersionComponentsPage();
        expect(await versionComponentsPage.getTitle()).to.eq('releaseVitalsApp.version.home.title');
    });

    it('should load create Version page', async () => {
        await versionComponentsPage.clickOnCreateButton();
        versionUpdatePage = new VersionUpdatePage();
        expect(await versionUpdatePage.getPageTitle()).to.eq('releaseVitalsApp.version.home.createOrEditLabel');
        await versionUpdatePage.cancel();
    });

    it('should create and save Versions', async () => {
        const nbButtonsBeforeCreate = await versionComponentsPage.countDeleteButtons();

        await versionComponentsPage.clickOnCreateButton();
        await promise.all([versionUpdatePage.setNameInput('name'), versionUpdatePage.issueSelectLastOption()]);
        expect(await versionUpdatePage.getNameInput()).to.eq('name');
        await versionUpdatePage.save();
        expect(await versionUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await versionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Version', async () => {
        const nbButtonsBeforeDelete = await versionComponentsPage.countDeleteButtons();
        await versionComponentsPage.clickOnLastDeleteButton();

        versionDeleteDialog = new VersionDeleteDialog();
        expect(await versionDeleteDialog.getDialogTitle()).to.eq('releaseVitalsApp.version.delete.question');
        await versionDeleteDialog.clickOnConfirmButton();

        expect(await versionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
