import { FoodUnit } from './food-unit';

export interface Food {
    id: number;
    name: string;
    brand?: string;
    protein: number;
    carbohydrate: number;
    fat: number;
    fiber: number;
    calories: number;
    units: FoodUnit[];
}
