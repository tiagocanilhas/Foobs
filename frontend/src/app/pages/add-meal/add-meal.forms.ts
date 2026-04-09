import { FormArray, FormControl, FormGroup } from "@angular/forms";

import { FoodUnit } from "@models/food-unit";

export interface AddMealForm {
  name: FormControl<string>;
  search: FormControl<string | null>;
  foods: FormArray<FormGroup<MealFoodForm>>;
}

export interface MealFoodForm {
  id: FormControl<number>;
  name: FormControl<string>;
  brand: FormControl<string | null>;
  quantity: FormControl<number>;
  availableUnits: FormControl<FoodUnit[]>;
  selectedUnit: FormControl<number>; 

  // These values are used for nutrition calculation
  protein: FormControl<number>;
  carbohydrate: FormControl<number>;
  fat: FormControl<number>;
  fiber: FormControl<number>;
  calories: FormControl<number>;
}


