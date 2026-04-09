import { Routes } from '@angular/router';

import { HomeComponent } from '@pages/home/home.component';
import { AddFoodComponent } from '@pages/add-food/add-food.component';
import { AddMealComponent } from '@pages/add-meal/add-meal.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent
    },
    {
        path: 'add-food',
        component: AddFoodComponent
    },
    {
        path: 'add-meal',
        component: AddMealComponent
    }
];
