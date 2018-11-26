/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EpicComponentsPage, EpicDeleteDialog, EpicUpdatePage } from './epic.page-object';

const expect = chai.expect;

describe('Epic e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let epicUpdatePage: EpicUpdatePage;
    let epicComponentsPage: EpicComponentsPage;
    let epicDeleteDialog: EpicDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Epics', async () => {
        await navBarPage.goToEntity('epic');
        epicComponentsPage = new EpicComponentsPage();
        expect(await epicComponentsPage.getTitle()).to.eq('releaseVitalsApp.epic.home.title');
    });

    it('should load create Epic page', async () => {
        await epicComponentsPage.clickOnCreateButton();
        epicUpdatePage = new EpicUpdatePage();
        expect(await epicUpdatePage.getPageTitle()).to.eq('releaseVitalsApp.epic.home.createOrEditLabel');
        await epicUpdatePage.cancel();
    });

    it('should create and save Epics', async () => {
        const nbButtonsBeforeCreate = await epicComponentsPage.countDeleteButtons();

        await epicComponentsPage.clickOnCreateButton();
        await promise.all([
            epicUpdatePage.setNameInput('name'),
            epicUpdatePage.setTotalStoryPointsInput('5'),
            epicUpdatePage.setStoryPointsCompletedInput('5'),
            epicUpdatePage.setRemainingStoryPointsInput('5'),
            epicUpdatePage.setTotalIssueCountInput('5'),
            epicUpdatePage.setPercentageCompletedInput('5'),
            epicUpdatePage.setKeyInput('key'),
            epicUpdatePage.setEpicBrowserURLInput('epicBrowserURL'),
            epicUpdatePage.projectSelectLastOption()
        ]);
        expect(await epicUpdatePage.getNameInput()).to.eq('name');
        expect(await epicUpdatePage.getTotalStoryPointsInput()).to.eq('5');
        expect(await epicUpdatePage.getStoryPointsCompletedInput()).to.eq('5');
        expect(await epicUpdatePage.getRemainingStoryPointsInput()).to.eq('5');
        expect(await epicUpdatePage.getTotalIssueCountInput()).to.eq('5');
        expect(await epicUpdatePage.getPercentageCompletedInput()).to.eq('5');
        expect(await epicUpdatePage.getKeyInput()).to.eq('key');
        expect(await epicUpdatePage.getEpicBrowserURLInput()).to.eq('epicBrowserURL');
        await epicUpdatePage.save();
        expect(await epicUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await epicComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Epic', async () => {
        const nbButtonsBeforeDelete = await epicComponentsPage.countDeleteButtons();
        await epicComponentsPage.clickOnLastDeleteButton();

        epicDeleteDialog = new EpicDeleteDialog();
        expect(await epicDeleteDialog.getDialogTitle()).to.eq('releaseVitalsApp.epic.delete.question');
        await epicDeleteDialog.clickOnConfirmButton();

        expect(await epicComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
