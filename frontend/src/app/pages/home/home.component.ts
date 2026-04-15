import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AsyncPipe } from '@angular/common';

import { MatInputModule } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { MatSliderModule } from '@angular/material/slider';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';

import { debounceTime, distinctUntilChanged, Observable, startWith, switchMap } from 'rxjs';

import { TranslateModule } from '@ngx-translate/core';

import { MealPanelComponent } from '@components/meal-panel/meal-panel.component';
import { MealCardComponent } from '@components/meal-card/meal-card.component';

import { MealService } from '@services/meal.service';

import { Meal } from '@models/meal';

import { SearchMealFilters, SortDirection, SortValue } from './home.forms';


@Component({
  selector: 'app-home',
  imports: [
    // Components
    MealPanelComponent,
    MealCardComponent,

    // Material
    MatInputModule,
    MatIcon,
    MatSliderModule,
    MatSelectModule,
    MatTooltipModule,

    // Forms
    ReactiveFormsModule,

    // Pipes
    AsyncPipe,

    // Translate
    TranslateModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  meals$!: Observable<Meal[]>;
  meal!: Meal;

  mealService: MealService = inject(MealService);

  constructor() { }

  sortOptions = Object.values(SortValue)
    // .map(value => ({ value, label: `SORT_VALUES.${value}` }));

  filters = new FormGroup<SearchMealFilters>({
    name: new FormControl('', { nonNullable: true }),
    minCalories: new FormControl(1, { nonNullable: true }),
    maxCalories: new FormControl(1000, { nonNullable: true }),
    sortValue: new FormControl(SortValue.NAME, { nonNullable: true }),
    sortDirection: new FormControl(SortDirection.ASC, { nonNullable: true })
  });


  ngOnInit(): void {
    this.meals$ = this.filters.valueChanges
      .pipe(
        startWith(this.filters.getRawValue()),
        distinctUntilChanged((a, b) => JSON.stringify(a) === JSON.stringify(b)),
        debounceTime(300),
        switchMap(value => {
          const { name, minCalories, maxCalories, sortValue, sortDirection } = value;

          return this.mealService.getMeals(
            name || '',
            minCalories || 0,
            maxCalories || 1000,
            sortValue || SortValue.NAME,
            sortDirection || SortDirection.ASC
          );
        })
      );
  }

  toggleDirection() {
    const dir = this.filters.controls.sortDirection;
    dir.setValue(dir.value === SortDirection.ASC ? SortDirection.DESC : SortDirection.ASC);
  }

  mealSelected(meal: Meal) {
    this.meal = meal;
  }

  closeMealPanel() {
    this.meal = undefined as any;
  }
}
