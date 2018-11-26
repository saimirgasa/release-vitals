/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { IssueComponentsPage, IssueDeleteDialog, IssueUpdatePage } from './issue.page-object';

const expect = chai.expect;

describe('Issue e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let issueUpdatePage: IssueUpdatePage;
    let issueComponentsPage: IssueComponentsPage;
    let issueDeleteDialog: IssueDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Issues', async () => {
        await navBarPage.goToEntity('issue');
        issueComponentsPage = new IssueComponentsPage();
        expect(await issueComponentsPage.getTitle()).to.eq('releaseVitalsApp.issue.home.title');
    });

    it('should load create Issue page', async () => {
        await issueComponentsPage.clickOnCreateButton();
        issueUpdatePage = new IssueUpdatePage();
        expect(await issueUpdatePage.getPageTitle()).to.eq('releaseVitalsApp.issue.home.createOrEditLabel');
        await issueUpdatePage.cancel();
    });

    it('should create and save Issues', async () => {
        const nbButtonsBeforeCreate = await issueComponentsPage.countDeleteButtons();

        await issueComponentsPage.clickOnCreateButton();
        await promise.all([
            issueUpdatePage.setTitleInput('title'),
            issueUpdatePage.setDescriptionInput('description'),
            issueUpdatePage.setKeyInput('key'),
            issueUpdatePage.sprintSelectLastOption(),
            issueUpdatePage.projectSelectLastOption(),
            issueUpdatePage.epicSelectLastOption()
        ]);
        expect(await issueUpdatePage.getTitleInput()).to.eq('title');
        expect(await issueUpdatePage.getDescriptionInput()).to.eq('description');
        expect(await issueUpdatePage.getKeyInput()).to.eq('key');
        await issueUpdatePage.save();
        expect(await issueUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await issueComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Issue', async () => {
        const nbButtonsBeforeDelete = await issueComponentsPage.countDeleteButtons();
        await issueComponentsPage.clickOnLastDeleteButton();

        issueDeleteDialog = new IssueDeleteDialog();
        expect(await issueDeleteDialog.getDialogTitle()).to.eq('releaseVitalsApp.issue.delete.question');
        await issueDeleteDialog.clickOnConfirmButton();

        expect(await issueComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
