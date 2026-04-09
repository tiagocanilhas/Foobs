import { MealFood } from './meal-food';

export interface Meal {
    id: number;
    name: string;
    url: string;
    protein: number;
    carbohydrate: number;
    fat: number;
    fiber: number;
    calories: number;
    foods: MealFood[];
}
