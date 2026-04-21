import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { AsyncPipe } from '@angular/common';

import { MatInputModule } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { MatSliderModule } from '@angular/material/slider';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatBottomSheet, MatBottomSheetModule } from '@angular/material/bottom-sheet';

import { debounceTime, distinctUntilChanged, Observable, startWith, switchMap } from 'rxjs';

import { TranslateModule } from '@ngx-translate/core';

import { LayoutService } from '@services/layout.service';

import { MealPanelComponent } from '@components/meal-panel/meal-panel.component';
import { MealCardComponent } from '@components/meal-card/meal-card.component';
import { FilterPanelComponent } from '@components/filter-panel/filter-panel.component';

import { MealService } from '@services/meal.service';

import { Meal } from '@models/meal';

import { SearchMealFilters, SortDirection, SortValue } from './home.forms';
import { ClosablePanelComponent } from "@components/closable-panel/closable-panel.component";


@Component({
  selector: 'app-home',
  imports: [
    // Components
    MealPanelComponent,
    MealCardComponent,
    FilterPanelComponent,
    // Material
    MatInputModule,
    MatIcon,
    MatSliderModule,
    MatSelectModule,
    MatTooltipModule,
    MatBottomSheetModule,
    // Forms
    ReactiveFormsModule,
    // Pipes
    AsyncPipe,
    // Translate
    TranslateModule,
    ClosablePanelComponent
],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  meals$!: Observable<Meal[]>;
  meal!: Meal;

  mealService: MealService = inject(MealService);

  layoutService = inject(LayoutService);

  private _bottomSheet = inject(MatBottomSheet);

  openFilters() {
    this._bottomSheet.open(FilterPanelComponent, {
      data: {
        filters: this.filters,
      }
    });
  }

  openMealPanel(meal: Meal) {
    this._bottomSheet.open(MealPanelComponent, {
      data: {
        meal: meal,
      }
    });
  }

  constructor() { }

  sortOptions = Object.values(SortValue)

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

  mealSelected(meal: Meal, isMobile: boolean | null) {
    this.meal = meal;

    if (isMobile) this.openMealPanel(meal);
  }

  closeMealPanel() {
    this.meal = undefined as any;
  }
}
