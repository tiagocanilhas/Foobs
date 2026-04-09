import { Component, inject } from '@angular/core';

import { MealPanelComponent } from '@components/meal-panel/meal-panel.component';
import { FilterPanelComponent } from "@components/filter-panel/filter-panel.component";
import { MealCardComponent } from '@components/meal-card/meal-card.component';

import { MealService } from '@services/meal.service';

import { Meal } from '@models/meal';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [
    // Components
    MealPanelComponent,
    FilterPanelComponent,
    MealCardComponent,

    // Pipes
    AsyncPipe,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  meals$: Observable<Meal[]>;
  meal!: Meal;
  
  mealService: MealService = inject(MealService);

  constructor() {
    this.meals$ = this.mealService.getMeals();
  }

  mealSelected(meal: Meal) {
    this.meal = meal;
  }

  closeMealPanel() {
    this.meal = undefined as any;
  }
}
