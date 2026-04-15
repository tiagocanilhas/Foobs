import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators, FormArray } from '@angular/forms';

import { TranslateModule } from '@ngx-translate/core';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { FoodService } from '@services/food.service';

import { AddFoodForm, FoodUnitForm } from './add-food.forms';

@Component({
  selector: 'app-add-food',
  imports: [
    ReactiveFormsModule,

    // Material
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,

    // Translate
    TranslateModule
  ],
  templateUrl: './add-food.component.html',
  styleUrl: './add-food.component.css'
})
export class AddFoodComponent {

  foodService: FoodService = inject(FoodService);

  form = new FormGroup<AddFoodForm>({name: new FormControl('', { 
      nonNullable: true, 
      validators: [Validators.required, Validators.minLength(3)] 
    }),
    brand: new FormControl(''),
    protein: new FormControl(0, { 
      nonNullable: true, 
      validators: [Validators.required, Validators.min(0)] 
    }),
    carbohydrate: new FormControl(0, { 
      nonNullable: true, 
      validators: [Validators.required, Validators.min(0)] 
    }),
    fat: new FormControl(0, { 
      nonNullable: true, 
      validators: [Validators.required, Validators.min(0)] 
    }),
    fiber: new FormControl(0, { 
      nonNullable: true, 
      validators: [Validators.required, Validators.min(0)] 
    }),
    units: new FormArray<FormGroup<FoodUnitForm>>([])
  });

  get units(): FormArray {
    return this.form.get('units') as FormArray;
  }

  addUnit() {
    const unit = new FormGroup<FoodUnitForm>({
      name: new FormControl('', { 
        nonNullable: true,
        validators: [Validators.required] 
      }),
      weight: new FormControl(100, { 
        nonNullable: true, 
        validators: [Validators.required, Validators.min(1)] 
      }),
    });

    this.units.push(unit);
  }

  removeUnit(index: number) {
    this.units.removeAt(index);
  }

  onSubmit() {
    const formValues = this.form.getRawValue()

    this.foodService.createFood(
      formValues.name,
      formValues.brand!,
      formValues.protein,
      formValues.carbohydrate,
      formValues.fat,
      formValues.fiber,
      formValues.units!
    ).subscribe({
      next: (res) => console.log(res),
      error: (err) => console.error(err)
    });
  }
}
