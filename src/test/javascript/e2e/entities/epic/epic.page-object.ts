import { element, by, ElementFinder } from 'protractor';

export class EpicComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-epic div table .btn-danger'));
    title = element.all(by.css('jhi-epic div h2#page-heading span')).first();

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

export class EpicUpdatePage {
    pageTitle = element(by.id('jhi-epic-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    totalStoryPointsInput = element(by.id('field_totalStoryPoints'));
    storyPointsCompletedInput = element(by.id('field_storyPointsCompleted'));
    remainingStoryPointsInput = element(by.id('field_remainingStoryPoints'));
    totalIssueCountInput = element(by.id('field_totalIssueCount'));
    percentageCompletedInput = element(by.id('field_percentageCompleted'));
    keyInput = element(by.id('field_key'));
    epicBrowserURLInput = element(by.id('field_epicBrowserURL'));
    projectSelect = element(by.id('field_project'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setTotalStoryPointsInput(totalStoryPoints) {
        await this.totalStoryPointsInput.sendKeys(totalStoryPoints);
    }

    async getTotalStoryPointsInput() {
        return this.totalStoryPointsInput.getAttribute('value');
    }

    async setStoryPointsCompletedInput(storyPointsCompleted) {
        await this.storyPointsCompletedInput.sendKeys(storyPointsCompleted);
    }

    async getStoryPointsCompletedInput() {
        return this.storyPointsCompletedInput.getAttribute('value');
    }

    async setRemainingStoryPointsInput(remainingStoryPoints) {
        await this.remainingStoryPointsInput.sendKeys(remainingStoryPoints);
    }

    async getRemainingStoryPointsInput() {
        return this.remainingStoryPointsInput.getAttribute('value');
    }

    async setTotalIssueCountInput(totalIssueCount) {
        await this.totalIssueCountInput.sendKeys(totalIssueCount);
    }

    async getTotalIssueCountInput() {
        return this.totalIssueCountInput.getAttribute('value');
    }

    async setPercentageCompletedInput(percentageCompleted) {
        await this.percentageCompletedInput.sendKeys(percentageCompleted);
    }

    async getPercentageCompletedInput() {
        return this.percentageCompletedInput.getAttribute('value');
    }

    async setKeyInput(key) {
        await this.keyInput.sendKeys(key);
    }

    async getKeyInput() {
        return this.keyInput.getAttribute('value');
    }

    async setEpicBrowserURLInput(epicBrowserURL) {
        await this.epicBrowserURLInput.sendKeys(epicBrowserURL);
    }

    async getEpicBrowserURLInput() {
        return this.epicBrowserURLInput.getAttribute('value');
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

export class EpicDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-epic-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-epic'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
