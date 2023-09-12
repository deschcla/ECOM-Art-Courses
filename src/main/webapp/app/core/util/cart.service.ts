import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Course } from '../request/course.model';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  counter: number = 0;
  cart: Course[] = [];

  counterChange: Subject<number> = new Subject<number>();

  constructor() {}

  addToCart(course: Course, num: number): void {
    for (let i = 0; i < num; i++) {
      this.cart.push(course);
    }
    this.counterChange.next((this.counter += num));
  }
}
