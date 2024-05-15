import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { User } from '../user';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';
import { catchError, switchMap, throwError } from 'rxjs';


@Component({
  selector: 'app-need-detail',
  templateUrl: './need-detail.component.html',
  styleUrls: [ './need-detail.component.css' ]
})
export class NeedDetailComponent implements OnInit {
  need: Need | undefined;
  user: User | undefined;
  basket: Need[] = [];
  quantity: number = 0;
  

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private userService: UserService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getNeed();
    this.userService.getBasket()
    .subscribe(basket => this.basket = basket);
  }

  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needService.getNeed(id)
      .subscribe(need => this.need = need);
  }

  addToBasket(): void {
    if(this.need) {
      this.userService.addToBasket(this.need)
        .subscribe(() => {
          this.userService.getBasket()
            .subscribe(basket => this.basket = basket);
        });
  }
}


   basketQuantity(need: Need): number {
    this.quantity = 0;
    for (let i = 0; i < this.basket.length; i++) {
      if (this.basket[i].id === need.id) {
        this.quantity++;
      }

    }
    return this.quantity;

  }
  

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.need) {
      this.needService.updateNeed(this.need)
        .subscribe(() => this.goBack());
    }
  }
}