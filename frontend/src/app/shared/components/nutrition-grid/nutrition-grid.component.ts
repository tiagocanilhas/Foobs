import { Component, Input } from '@angular/core';
import { DecimalPipe } from '@angular/common';

import { TmNgOdometerModule } from 'tm-ng-odometer';

@Component({
  selector: 'app-nutrition-grid',
  imports: [
    TmNgOdometerModule
  ],
  templateUrl: './nutrition-grid.component.html',
  styleUrls: ['./nutrition-grid.component.css']
})
export class NutritionGridComponent {
  @Input({ required: true }) calories: number = 0;
  @Input({ required: true }) protein: number = 0;
  @Input({ required: true }) fat: number = 0;
  @Input({ required: true }) carbohydrate: number = 0;
  @Input({ required: true }) fiber: number = 0;
}

