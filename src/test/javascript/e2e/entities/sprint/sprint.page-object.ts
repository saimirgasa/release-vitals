import { element, by, ElementFinder } from 'protractor';

export class SprintComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-sprint div table .btn-danger'));
    title = element.all(by.css('jhi-sprint div h2#page-heading span')).first();

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

export class SprintUpdatePage {
    pageTitle = element(by.id('jhi-sprint-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    startDatetimeInput = element(by.id('field_startDatetime'));
    endDatetimeInput = element(by.id('field_endDatetime'));
    velocityInput = element(by.id('field_velocity'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setStartDatetimeInput(startDatetime) {
        await this.startDatetimeInput.sendKeys(startDatetime);
    }

    async getStartDatetimeInput() {
        return this.startDatetimeInput.getAttribute('value');
    }

    async setEndDatetimeInput(endDatetime) {
        await this.endDatetimeInput.sendKeys(endDatetime);
    }

    async getEndDatetimeInput() {
        return this.endDatetimeInput.getAttribute('value');
    }

    async setVelocityInput(velocity) {
        await this.velocityInput.sendKeys(velocity);
    }

    async getVelocityInput() {
        return this.velocityInput.getAttribute('value');
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

export class SprintDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-sprint-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-sprint'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
