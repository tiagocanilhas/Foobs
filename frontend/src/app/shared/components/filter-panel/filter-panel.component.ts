import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Component, inject, Input, OnInit } from '@angular/core';

import { MatInputModule} from "@angular/material/input";
import { MatFormFieldModule } from '@angular/material/form-field';

import { TranslateModule } from '@ngx-translate/core';
import { MAT_BOTTOM_SHEET_DATA } from '@angular/material/bottom-sheet';

@Component({
  selector: 'app-filter-panel',
  imports: [
    // Material
    MatFormFieldModule,
    MatInputModule,

    // Forms
    ReactiveFormsModule,
    
    // Translate
    TranslateModule
],
  templateUrl: './filter-panel.component.html',
  styleUrl: './filter-panel.component.css'
})
export class FilterPanelComponent implements OnInit {
  public data = inject(MAT_BOTTOM_SHEET_DATA, { optional: true });

  @Input() filters!: FormGroup;

  ngOnInit(): void {
    if (this.data) {
      this.filters = this.data.filters;
    }
  }
}