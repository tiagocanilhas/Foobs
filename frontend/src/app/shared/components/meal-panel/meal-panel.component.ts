import { Component, Input, Output, EventEmitter } from '@angular/core';

import { ClosablePanelComponent } from "@components/closable-panel/closable-panel.component";

import { Meal } from '@models/meal';

import { NutritionGridComponent } from '@components/nutrition-grid/nutrition-grid.component';


@Component({
  selector: 'app-meal-panel',
  imports: [
    ClosablePanelComponent,
    NutritionGridComponent
  ],
  templateUrl: './meal-panel.component.html',
  styleUrl: './meal-panel.component.css'
})
export class MealPanelComponent {
  @Input() meal!: Meal;

  @Output() close = new EventEmitter<void>();

  onClose() {
    this.close.emit();
  }

  closeButtonDisabled(): boolean {
    return !this.meal
  }
}
