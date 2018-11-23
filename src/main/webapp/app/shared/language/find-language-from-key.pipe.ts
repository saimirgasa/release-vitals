import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'findLanguageFromKey' })
export class FindLanguageFromKeyPipe implements PipeTransform {
    private languages: any = {
        al: { name: 'Shqip' },
        cs: { name: 'Český' },
        en: { name: 'English' },
        fr: { name: 'Français' },
        de: { name: 'Deutsch' },
        it: { name: 'Italiano' },
        ru: { name: 'Русский' },
        es: { name: 'Español' },
        vi: { name: 'Tiếng Việt' }
        // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
    };
    transform(lang: string): string {
        return this.languages[lang].name;
    }
}
