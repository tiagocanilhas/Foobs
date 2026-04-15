import { Component, OnInit } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';

import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';

import { LanguageSelectorComponent } from '@components/language-selector/language-selector.component';


@Component({
  selector: 'app-root',
  imports: [
    // Router
    RouterOutlet,
    RouterLink,

    // Components
    LanguageSelectorComponent,

    // Material Modules
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,

    // Translate
    TranslateModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  constructor(private translate: TranslateService) {
    translate.addLangs(['pt', 'en', 'es', 'fr', 'de']);
    translate.setDefaultLang('en');
  }

  ngOnInit() {
    const savedLang = localStorage.getItem('user_lang');
    const browserLang = this.translate.getBrowserLang(); 
    const finalLang = savedLang || (browserLang?.match(/pt|en|es|fr|de/) ? browserLang : 'en');
    this.translate.use(finalLang);
  }

  title = 'Foobs';
  
  sidenavOpened = false;
  
  toggleSidenav() {
    this.sidenavOpened = !this.sidenavOpened;
  }
}
