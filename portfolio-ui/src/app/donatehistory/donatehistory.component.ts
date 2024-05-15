import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { User } from '../user';
import { Need } from '../need';
import { NeedService } from '../need.service';

@Component({
  selector: 'app-donatehistory',
  templateUrl: './donatehistory.component.html',
  styleUrls: ['./donatehistory.component.css']
})
export class DonationHistoryComponent implements OnInit {
  needs: Need[] = []; // Assuming you have a Need model

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.getDonationHistory();
  }

  getDonationHistory(): void {
    this.userService.getHistory()
      .subscribe(needs => this.needs = needs);
      
  }
}
