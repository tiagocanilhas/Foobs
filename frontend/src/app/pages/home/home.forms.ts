import { FormControl } from "@angular/forms";

export enum SortValue {
  NAME = 'NAME',
  CALORIES = 'CALORIES',
  PROTEIN = 'PROTEIN',
  CARBOHYDRATE = 'CARBOHYDRATE',
  FAT = 'FAT',
  FIBER = 'FIBER'
}

export enum SortDirection {
  ASC = 'ASC',
  DESC = 'DESC'
}

export interface SearchMealFilters {
  name: FormControl<string>;
  minCalories: FormControl<number>;
  maxCalories: FormControl<number>;
  sortValue: FormControl<SortValue>;
  sortDirection: FormControl<SortDirection>;
}
