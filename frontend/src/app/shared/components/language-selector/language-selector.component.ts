import { Component } from '@angular/core';
import { UpperCasePipe } from '@angular/common';

import { TranslateService, TranslateModule } from '@ngx-translate/core';

import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-language-selector',
  imports: [
    // Material
    MatMenuModule,
    MatButtonModule,
    MatIconModule,

    // Pipe,
    UpperCasePipe,

    // Translate
    TranslateModule
  ],
  templateUrl: './language-selector.component.html',
  styleUrl: './language-selector.component.css'
})
export class LanguageSelectorComponent {
  constructor(private translate: TranslateService) {}

  get currentLang() {
    return this.translate.currentLang || this.translate.defaultLang;
  }

  changeLang(lang: string) {
    this.translate.use(lang);
    localStorage.setItem('user_lang', lang);
  }
}
