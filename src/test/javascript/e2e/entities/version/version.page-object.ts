import { element, by, ElementFinder } from 'protractor';

export class VersionComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-version div table .btn-danger'));
    title = element.all(by.css('jhi-version div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class VersionUpdatePage {
    pageTitle = element(by.id('jhi-version-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    projectSelect = element(by.id('field_project'));
    issueSelect = element(by.id('field_issue'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async projectSelectLastOption() {
        await this.projectSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async projectSelectOption(option) {
        await this.projectSelect.sendKeys(option);
    }

    getProjectSelect(): ElementFinder {
        return this.projectSelect;
    }

    async getProjectSelectedOption() {
        return this.projectSelect.element(by.css('option:checked')).getText();
    }

    async issueSelectLastOption() {
        await this.issueSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async issueSelectOption(option) {
        await this.issueSelect.sendKeys(option);
    }

    getIssueSelect(): ElementFinder {
        return this.issueSelect;
    }

    async getIssueSelectedOption() {
        return this.issueSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class VersionDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-version-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-version'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
