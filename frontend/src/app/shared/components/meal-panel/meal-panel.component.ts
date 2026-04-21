import { Component, Input, Output, EventEmitter, OnInit, inject } from '@angular/core';

import { TranslateModule } from '@ngx-translate/core';

import { ClosablePanelComponent } from "@components/closable-panel/closable-panel.component";
import { NutritionGridComponent } from '@components/nutrition-grid/nutrition-grid.component';

import { Meal } from '@models/meal';
import { MAT_BOTTOM_SHEET_DATA } from '@angular/material/bottom-sheet';


@Component({
  selector: 'app-meal-panel',
  imports: [
    NutritionGridComponent,

    // Translate
    TranslateModule
  ],
  templateUrl: './meal-panel.component.html',
  styleUrl: './meal-panel.component.css'
})
export class MealPanelComponent implements OnInit {
  public data = inject(MAT_BOTTOM_SHEET_DATA, { optional: true });

  @Input() meal!: Meal;

  ngOnInit(): void {
    if (this.data) {
      this.meal = this.data.meal;
    }
  }
}
