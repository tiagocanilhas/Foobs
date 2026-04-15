import { Component, inject, OnInit } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators, FormArray } from '@angular/forms';
import { AsyncPipe } from '@angular/common';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatSelectModule } from '@angular/material/select';

import { debounceTime, distinctUntilChanged, Observable, of, switchMap } from 'rxjs';

import { TranslateModule } from '@ngx-translate/core';

import { NutritionGridComponent } from '@components/nutrition-grid/nutrition-grid.component';

import { FoodService } from '@services/food.service';
import { MealService } from '@services/meal.service';

import { Food } from '@models/food';

import { AddMealForm, MealFoodForm } from './add-meal.forms';

@Component({
  selector: 'app-add-meal',
  imports: [
    // Components
    NutritionGridComponent,

    // Forms
    ReactiveFormsModule,

    // Material
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatAutocompleteModule,
    MatSelectModule,

    // Pipes
    AsyncPipe,

    // Translate
    TranslateModule
  ],
  templateUrl: './add-meal.component.html',
  styleUrl: './add-meal.component.css'
})
export class AddMealComponent implements OnInit {
  private foodService = inject(FoodService);
  private mealService = inject(MealService);

  filteredFoods$: Observable<Food[]> | undefined;

  ngOnInit(): void {
    this.filteredFoods$ = this.form.controls.search.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap(value => {
          if (typeof value !== 'string' || value.length < 2)  return of([]);
          
          return this.foodService.getFoods(value);
        }
      ));

    this.form.valueChanges
    .pipe(
      debounceTime(100)
    ).subscribe(() => {
      this.calculateTotals();
    });
  }


  form = new FormGroup<AddMealForm>({
    name: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    search: new FormControl(''),
    foods: new FormArray<FormGroup<MealFoodForm>>([]),
  });

  get foods() {
    return this.form.controls.foods;
  }



  /**  
   *  Nutrition calculation
   */

  totals = {
    calories: 0,
    protein: 0,
    carbohydrate: 0,
    fat: 0,
    fiber: 0
  };

  calculateTotals() {
    const foodsArray = this.form.getRawValue().foods;

    const newTotals = { calories: 0, protein: 0, carbohydrate: 0, fat: 0, fiber: 0 };

    foodsArray.forEach(f => {
      const unit = f.availableUnits.find(u => u.id === f.selectedUnit);
      const unitWeight = unit?.weight || 1;

      const factor = (f.quantity * unitWeight) / 100;

      newTotals.protein += f.protein * factor;
      newTotals.carbohydrate += f.carbohydrate * factor;
      newTotals.fat += f.fat * factor;
      newTotals.fiber += f.fiber * factor;
      newTotals.calories += f.calories * factor;
    });

    this.totals = newTotals;
  }



  /**
   *  Manage foods in the meal
   */

  addFood(food: Food) {
    const units = food.units || [];

    const foodGroup = new FormGroup<MealFoodForm>({
      id: new FormControl(food.id, { nonNullable: true }),
      name: new FormControl(food.name, { nonNullable: true }),
      brand: new FormControl(food.brand || null),
      quantity: new FormControl(100, { nonNullable: true, validators: [Validators.required, Validators.min(1)] }),
      availableUnits: new FormControl(units, { nonNullable: true }),
      selectedUnit: new FormControl(units[0]?.id || 0, { nonNullable: true, validators: [Validators.required] }),

      // These values are used for nutrition calculation
      protein: new FormControl(food.protein, { nonNullable: true }),
      carbohydrate: new FormControl(food.carbohydrate, { nonNullable: true }),
      fat: new FormControl(food.fat, { nonNullable: true }),
      fiber: new FormControl(food.fiber, { nonNullable: true }),
      calories: new FormControl(food.calories, { nonNullable: true }),
    });

    this.foods.push(foodGroup);
    this.form.controls.search.setValue('');
  }

  removeFood(index: number) {
    this.foods.removeAt(index);
  }



  onSubmit() {
    const formValues = this.form.getRawValue()

    this.mealService.createMeal(
      formValues.name,
      formValues.foods.map(f => ({
        id: f.id,
        quantity: f.quantity,
        unitId: f.selectedUnit,
      }))
    ).subscribe({
      next: res => console.log(res),
      error: err => console.error(err)
    });
  }

}
