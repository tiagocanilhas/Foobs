import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import { Meal } from '@models/meal';

interface MealResponse {
  value: Meal[];
}

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

  getMeals(): Observable<Meal[]> {
    return this.http.get<MealResponse>('/api/meal')
      .pipe(map(res => res.value));
  }
}
