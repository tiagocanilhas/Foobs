import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import { Meal } from '@models/meal';

@Injectable({
  providedIn: 'root'
})
export class MealService {

  private http = inject(HttpClient);

  constructor() { }

  createMeal(
    name: string,
    foods: { id: number, quantity: number, unitId: number }[]
  ): Observable<Meal> {

    return this.http.post<Meal>('/api/meal', {
      name,
      foods
    });
  }

  getMeals(
    name: string,
    minCalories: number,
    maxCalories: number,
    sortValue: string,
    sortDirection: string
  ): Observable<Meal[]> {

    console.log({ name, minCalories, maxCalories, sortValue, sortDirection });

    const params = new HttpParams()
      .set('name', name)
      .set('minCalories', minCalories)
      .set('maxCalories', maxCalories)
      .set('sortValue', sortValue)
      .set('sortDirection', sortDirection);

    return this.http.get<{ meals: Meal[] }>('/api/meal', { params })
      .pipe(map(res => res.meals));
  }
}
