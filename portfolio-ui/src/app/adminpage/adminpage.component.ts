import { Component } from '@angular/core';
import { Need } from '../need';
import { User } from '../user';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-adminpage',
  templateUrl: './adminpage.component.html',
  styleUrls: ['./adminpage.component.css']
})
export class AdminpageComponent {
  needs: Need[] = [];
  users: User[] = [];
  constructor(private needService: NeedService, private userService: UserService) { }
  ngOnInit(): void {
    this.getNeeds();
    this.getUsers();
  }

  getNeeds(): void {
    this.needService.getNeeds()
    .subscribe(needs => this.needs = needs);
  }

  getUsers(): void {
    this.userService.getUsers()
    .subscribe(users => this.users = users);
  }
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
  deleteUser(user: User): void {
    this.users = this.users.filter(n => n !== user);
    this.userService.deleteUser(user.username).subscribe();
  }

  convertString(string : string) {
    return parseInt(string);
  }
}