import { element, by, ElementFinder } from 'protractor';

export class ProjectComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-project div table .btn-danger'));
    title = element.all(by.css('jhi-project div h2#page-heading span')).first();

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

export class ProjectUpdatePage {
    pageTitle = element(by.id('jhi-project-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    keyInput = element(by.id('field_key'));
    releaseSelect = element(by.id('field_release'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setKeyInput(key) {
        await this.keyInput.sendKeys(key);
    }

    async getKeyInput() {
        return this.keyInput.getAttribute('value');
    }

    async releaseSelectLastOption() {
        await this.releaseSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async releaseSelectOption(option) {
        await this.releaseSelect.sendKeys(option);
    }

    getReleaseSelect(): ElementFinder {
        return this.releaseSelect;
    }

    async getReleaseSelectedOption() {
        return this.releaseSelect.element(by.css('option:checked')).getText();
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

export class ProjectDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-project-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-project'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
