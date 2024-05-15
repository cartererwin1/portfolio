import { Component } from '@angular/core';
import { UserService } from './user.service';
import { Observable } from 'rxjs';
import { User } from './user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  total: number | any;
  constructor(private userService: UserService, private router: Router) { }
  

  title = 'Palestine War Refugee Fund';
  currentUser = this.userService.getCurrentUserName();
  logout() {
    this.userService.logout();
  }
  isNotLoginPage() : boolean {
    if(this.router.url === "http://localhost:4200/login") {
      return false;
    }
    return true;
  }
  

}