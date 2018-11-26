/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SprintComponentsPage, SprintDeleteDialog, SprintUpdatePage } from './sprint.page-object';

const expect = chai.expect;

describe('Sprint e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let sprintUpdatePage: SprintUpdatePage;
    let sprintComponentsPage: SprintComponentsPage;
    let sprintDeleteDialog: SprintDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Sprints', async () => {
        await navBarPage.goToEntity('sprint');
        sprintComponentsPage = new SprintComponentsPage();
        expect(await sprintComponentsPage.getTitle()).to.eq('releaseVitalsApp.sprint.home.title');
    });

    it('should load create Sprint page', async () => {
        await sprintComponentsPage.clickOnCreateButton();
        sprintUpdatePage = new SprintUpdatePage();
        expect(await sprintUpdatePage.getPageTitle()).to.eq('releaseVitalsApp.sprint.home.createOrEditLabel');
        await sprintUpdatePage.cancel();
    });

    it('should create and save Sprints', async () => {
        const nbButtonsBeforeCreate = await sprintComponentsPage.countDeleteButtons();

        await sprintComponentsPage.clickOnCreateButton();
        await promise.all([
            sprintUpdatePage.setNameInput('name'),
            sprintUpdatePage.setStartDatetimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            sprintUpdatePage.setEndDatetimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            sprintUpdatePage.setVelocityInput('5')
        ]);
        expect(await sprintUpdatePage.getNameInput()).to.eq('name');
        expect(await sprintUpdatePage.getStartDatetimeInput()).to.contain('2001-01-01T02:30');
        expect(await sprintUpdatePage.getEndDatetimeInput()).to.contain('2001-01-01T02:30');
        expect(await sprintUpdatePage.getVelocityInput()).to.eq('5');
        await sprintUpdatePage.save();
        expect(await sprintUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await sprintComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Sprint', async () => {
        const nbButtonsBeforeDelete = await sprintComponentsPage.countDeleteButtons();
        await sprintComponentsPage.clickOnLastDeleteButton();

        sprintDeleteDialog = new SprintDeleteDialog();
        expect(await sprintDeleteDialog.getDialogTitle()).to.eq('releaseVitalsApp.sprint.delete.question');
        await sprintDeleteDialog.clickOnConfirmButton();

        expect(await sprintComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
