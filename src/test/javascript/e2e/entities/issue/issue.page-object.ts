import { element, by, ElementFinder } from 'protractor';

export class IssueComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-issue div table .btn-danger'));
    title = element.all(by.css('jhi-issue div h2#page-heading span')).first();

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

export class IssueUpdatePage {
    pageTitle = element(by.id('jhi-issue-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    titleInput = element(by.id('field_title'));
    descriptionInput = element(by.id('field_description'));
    keyInput = element(by.id('field_key'));
    sprintSelect = element(by.id('field_sprint'));
    projectSelect = element(by.id('field_project'));
    epicSelect = element(by.id('field_epic'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setTitleInput(title) {
        await this.titleInput.sendKeys(title);
    }

    async getTitleInput() {
        return this.titleInput.getAttribute('value');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async setKeyInput(key) {
        await this.keyInput.sendKeys(key);
    }

    async getKeyInput() {
        return this.keyInput.getAttribute('value');
    }

    async sprintSelectLastOption() {
        await this.sprintSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async sprintSelectOption(option) {
        await this.sprintSelect.sendKeys(option);
    }

    getSprintSelect(): ElementFinder {
        return this.sprintSelect;
    }

    async getSprintSelectedOption() {
        return this.sprintSelect.element(by.css('option:checked')).getText();
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

    async epicSelectLastOption() {
        await this.epicSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async epicSelectOption(option) {
        await this.epicSelect.sendKeys(option);
    }

    getEpicSelect(): ElementFinder {
        return this.epicSelect;
    }

    async getEpicSelectedOption() {
        return this.epicSelect.element(by.css('option:checked')).getText();
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

export class IssueDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-issue-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-issue'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
