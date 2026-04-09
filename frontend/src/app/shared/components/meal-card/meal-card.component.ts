import { Component, Input, Output, EventEmitter, HostListener} from '@angular/core';

import { Meal } from '@models/meal';

@Component({
  selector: 'app-meal-card',
  imports: [],
  templateUrl: './meal-card.component.html',
  styleUrl: './meal-card.component.css'
})
export class MealCardComponent {
  @Input() meal!: Meal;

  @Output() selected = new EventEmitter<Meal>();

  @HostListener('click')
  onClick() {
    this.selected.emit(this.meal);
  }
}
