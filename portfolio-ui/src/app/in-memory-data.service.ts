import { Injectable } from '@angular/core';
import { InMemoryDbService } from 'angular-in-memory-web-api';
import { Need } from './need';

@Injectable({
  providedIn: 'root',
})
export class InMemoryDataService implements InMemoryDbService {
  createDb() {
    const needs = [
      { id: 12, name: 'soup', cost: 1, type: "food", quantity: 1 },
      { id: 13, name: 'soup', cost: 1, type: "food", quantity: 1 },
      { id: 14, name: 'soup', cost: 1, type: "food", quantity: 1 },
      { id: 15, name: 'soup', cost: 1, type: "food", quantity: 1 },
      { id: 16, name: 'soup', cost: 1, type: "food", quantity: 1 },
      { id: 17, name: 'soup', cost: 1, type: "food", quantity: 1 },
    ];
    return {needs};
  }

  // Overrides the genId method to ensure that a hero always has an id.
  // If the heroes array is empty,
  // the method below returns the initial number (11).
  // if the heroes array is not empty, the method below returns the highest
  // hero id + 1.
  genId(needs: Need[]): number {
    return needs.length > 0 ? Math.max(...needs.map(need => need.id)) + 1 : 11;
  }
}