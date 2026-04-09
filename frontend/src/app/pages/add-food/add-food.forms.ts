import { FormArray, FormControl, FormGroup } from "@angular/forms";

export interface AddFoodForm {
    name: FormControl<string>;
    brand: FormControl<string | null>;
    protein: FormControl<number>;
    carbohydrate: FormControl<number>;
    fat: FormControl<number>;
    fiber: FormControl<number>;
    units: FormArray<FormGroup<FoodUnitForm>>;
}

export interface FoodUnitForm {
  name: FormControl<string>;
  weight: FormControl<number>;
}