import { element, by, ElementFinder } from 'protractor';

export class StatusComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-status div table .btn-danger'));
    title = element.all(by.css('jhi-status div h2#page-heading span')).first();

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

export class StatusUpdatePage {
    pageTitle = element(by.id('jhi-status-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    fromStatusInput = element(by.id('field_fromStatus'));
    toStatusInput = element(by.id('field_toStatus'));
    timeChagedInput = element(by.id('field_timeChaged'));
    issueSelect = element(by.id('field_issue'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setFromStatusInput(fromStatus) {
        await this.fromStatusInput.sendKeys(fromStatus);
    }

    async getFromStatusInput() {
        return this.fromStatusInput.getAttribute('value');
    }

    async setToStatusInput(toStatus) {
        await this.toStatusInput.sendKeys(toStatus);
    }

    async getToStatusInput() {
        return this.toStatusInput.getAttribute('value');
    }

    async setTimeChagedInput(timeChaged) {
        await this.timeChagedInput.sendKeys(timeChaged);
    }

    async getTimeChagedInput() {
        return this.timeChagedInput.getAttribute('value');
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

export class StatusDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-status-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-status'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
