import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedsComponent } from './needs/needs.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { MessagesComponent } from './messages/messages.component';
import { RouterOutlet } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AdminpageComponent } from './adminpage/adminpage.component';
import { BasketComponent } from './basket/basket.component';
import { AdminDetailComponent } from './admin-detail/admin-detail.component';
import { DonationHistoryComponent } from './donatehistory/donatehistory.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    RouterOutlet,

    // The HttpClientInMemoryWebApiModule module intercepts HTTP requests
    // and returns simulated server responses.
    // Remove it when a real server is ready to receive requests.
    
  ],
  declarations: [
    AppComponent,
    DashboardComponent,
    NeedsComponent,
    NeedDetailComponent,
    MessagesComponent,
    NeedSearchComponent,
    LoginComponent,
    AdminpageComponent,
    BasketComponent,
    AdminDetailComponent,
    DonationHistoryComponent,
    LeaderboardComponent,
    
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }