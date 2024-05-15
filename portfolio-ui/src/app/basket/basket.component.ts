import { Component } from '@angular/core';
import { User } from '../user';
import { Need } from '../need';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';
import { UnaryFunction } from 'rxjs';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.css']
})
export class BasketComponent {
  needs: Need[] = [];
  user: User | undefined;
  total: number = 0;
  quantity: any;
  quantities: Map<String, Number> = new Map;
  num: Number = 0;

  constructor(private needService: NeedService, private userService: UserService) { }
  ngOnInit(): void {
    this.getNeeds();
    this.getUser();
    this.getTotal();
    
    
  }

  getNeeds(): void {
    this.userService.getBasket()
    .subscribe(needs => this.needs = needs);
  }

  getUser(): void {
    this.userService.getUser(this.userService.getCurrentUserName() as string)
    .subscribe(user => this.user = user);
  }
  removeFromBasket(id : number ): void {
    this.userService.removeFromBasket(id)
    .subscribe(needs => this.needs = needs);
    location.reload();
    
  }

  basketQuantity(need: Need): void {
    for (let i = 0; i < this.needs.length; i++) {
      if (this.needs[i].type === need.type) {
        this.quantities.set(need.type, this.quantity++);
      }
      else{
        this.quantity = 0;
      }

    }

  }
  checkout() : void {
    for(let i =0; i < this.needs.length; i++){
      this.basketQuantity(this.needs[i]);
      if(this.quantities.has(this.needs[i].type)){
        this.needs[i].quantity -= 1;
        this.needService.updateNeed(this.needs[i]).subscribe();
      }
      if(this.needs[i].quantity === 0  ){
        this.needService.deleteNeed(this.needs[i].id).subscribe();
      }
     
         
     
      }
      
    
    this.userService.checkout()
    .subscribe(total => this.total = total);
    location.reload();

  }
  getTotal() : void{
    if(this.needs.length != null){
      this.userService.getTotal()
        .subscribe(total => this.total = total);
    }
    else{
      this.total = 0;
    }
    
  }

  getTotalTwo(): number{
    return this.userService.getTotalTwo();

  }
 
}
