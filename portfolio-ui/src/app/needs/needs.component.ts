import { Component, OnInit } from '@angular/core';

import { Need } from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-needs',
  templateUrl: './needs.component.html',
  styleUrls: ['./needs.component.css']
  
})
export class NeedsComponent implements OnInit {
  needs: Need[] = [];

  constructor(private needService: NeedService) { }

  ngOnInit(): void {
    this.getNeeds();
  }

  getNeeds(): void {
    this.needService.getNeeds()
    .subscribe(needs => this.needs = needs);
  }
  
  //add(name: string): void {
  add(name: string, cost: number, type: string, quantity: number): void {
    name = name.trim();
    //type = type.trim();
    if (!name) { return; }
    this.needService.addNeed({name, cost, type, quantity } as Need)
      .subscribe(need => {
        this.needs.push(need);
      });
  }

  delete(need: Need): void {
    this.needs = this.needs.filter(n => n !== need);
    this.needService.deleteNeed(need.id).subscribe();
  }

  convertString(string : string) {
    return parseInt(string);
  }

}