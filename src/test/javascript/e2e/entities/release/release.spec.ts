/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ReleaseComponentsPage, ReleaseDeleteDialog, ReleaseUpdatePage } from './release.page-object';

const expect = chai.expect;

describe('Release e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let releaseUpdatePage: ReleaseUpdatePage;
    let releaseComponentsPage: ReleaseComponentsPage;
    let releaseDeleteDialog: ReleaseDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Releases', async () => {
        await navBarPage.goToEntity('release');
        releaseComponentsPage = new ReleaseComponentsPage();
        expect(await releaseComponentsPage.getTitle()).to.eq('releaseVitalsApp.release.home.title');
    });

    it('should load create Release page', async () => {
        await releaseComponentsPage.clickOnCreateButton();
        releaseUpdatePage = new ReleaseUpdatePage();
        expect(await releaseUpdatePage.getPageTitle()).to.eq('releaseVitalsApp.release.home.createOrEditLabel');
        await releaseUpdatePage.cancel();
    });

    it('should create and save Releases', async () => {
        const nbButtonsBeforeCreate = await releaseComponentsPage.countDeleteButtons();

        await releaseComponentsPage.clickOnCreateButton();
        await promise.all([
            releaseUpdatePage.setNameInput('name'),
            releaseUpdatePage.setStartDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            releaseUpdatePage.setEndDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM')
        ]);
        expect(await releaseUpdatePage.getNameInput()).to.eq('name');
        expect(await releaseUpdatePage.getStartDateInput()).to.contain('2001-01-01T02:30');
        expect(await releaseUpdatePage.getEndDateInput()).to.contain('2001-01-01T02:30');
        await releaseUpdatePage.save();
        expect(await releaseUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await releaseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Release', async () => {
        const nbButtonsBeforeDelete = await releaseComponentsPage.countDeleteButtons();
        await releaseComponentsPage.clickOnLastDeleteButton();

        releaseDeleteDialog = new ReleaseDeleteDialog();
        expect(await releaseDeleteDialog.getDialogTitle()).to.eq('releaseVitalsApp.release.delete.question');
        await releaseDeleteDialog.clickOnConfirmButton();

        expect(await releaseComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
