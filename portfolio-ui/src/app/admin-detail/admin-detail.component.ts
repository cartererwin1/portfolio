import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Need } from '../need';
import { User } from '../user';
import { NeedService } from '../need.service';
import { UserService } from '../user.service';


@Component({
  selector: 'app-admin-detail',
  templateUrl: './admin-detail.component.html',
  styleUrls: [ './admin-detail.component.css' ]
})
export class AdminDetailComponent implements OnInit {
  need: Need | undefined;
  user: User | undefined;

  constructor(
    private route: ActivatedRoute,
    private needService: NeedService,
    private userService: UserService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getNeed();
  }

  getNeed(): void {
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needService.getNeed(id)
      .subscribe(need => this.need = need);
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