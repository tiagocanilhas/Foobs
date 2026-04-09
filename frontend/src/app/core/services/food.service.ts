import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { map, Observable } from 'rxjs';

import { Food } from '@models/food';

@Injectable({
  providedIn: 'root'
})
export class FoodService {
  private http = inject(HttpClient);

  constructor() { }

  createFood(
    name: string,
    brand: string,
    protein: number,
    carbohydrate: number,
    fat: number,
    fiber: number,
    units: { name: string, weight: number }[]
  ): Observable<Food> {
    const brandValue = brand?.trim() === "" ? null : brand;
    
    return this.http.post<Food>('/api/food', {
      name,
      brand: brandValue,
      protein,
      carbohydrate,
      fat,
      fiber,
      units
    })
  }

  getFoods(name: string): Observable<Food[]> {
    let params = new HttpParams()

    if (name) params = params.set('name', name);
    
    return this.http.get<{ food: Food[] }>(`/api/food`, { params })
      .pipe(map(res => res.food));
  }
}
