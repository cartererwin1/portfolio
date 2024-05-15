import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { Observable, of, BehaviorSubject } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { User } from './user';
import { MessageService } from './message.service';
import { Need } from './need';


@Injectable({ providedIn: 'root' })
export class UserService {

  private usersUrl = 'http://localhost:8080/users';  // URL to web api
  private currentUserSubject: BehaviorSubject<any>;
  public currentUser: Observable<any>;
  currentUsername: string | null = null;
  total: number | any = 0;
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { 
      this.currentUserSubject = new BehaviorSubject<any>(null);
      this.currentUser = this.currentUserSubject.asObservable();

    }

  
  
  isNewUser(username:string): Observable<boolean> {
    return this.http.get<boolean>(this.usersUrl + "/isNewUser")
  }

  /** GET heroes from the server */
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl)
      .pipe(
        tap(_ => this.log('fetched users')),
        catchError(this.handleError<User[]>('getUsers', []))
      );
  }

  /** GET hero by id. Return `undefined` when id not found */
  getUserNo404<Data>(id: number): Observable<User> {
    const url = `${this.usersUrl}/?id=${id}`;
    return this.http.get<User[]>(url)
      .pipe(
        map(users => users[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? 'fetched' : 'did not find';
          this.log(`${outcome} user id=${id}`);
        }),
        catchError(this.handleError<User>(`getUser id=${id}`))
      );
  }
  login(username:string, password: string): Observable <User> {
    if(this.isNewUser(username)) {
      localStorage.setItem('currentUser', JSON.stringify(username));
      return this.http.post<User>(this.usersUrl + "/login", {username, password})
    } else {
      localStorage.setItem('currentUser', JSON.stringify(username));
      return this.http.get<User>(this.usersUrl + "/" + username)
    }
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    //this.currentUserSubject.next(null);
    window.location.href="http://localhost:4200";
  }

  /** GET hero by id. Will 404 if id not found */
  getUser(username: string): Observable<User> {
    const url = `${this.usersUrl}/${username}`;
    return this.http.get<User>(url).pipe(
      tap(_ => this.log(`fetched user id=${username}`)),
      catchError(this.handleError<User>(`getUser username=${username}`))
    );
  }
  getCurrentUserName(): String {
    this.currentUsername = localStorage.getItem("currentUser");
    if(this.currentUsername) {
      return this.currentUsername;
    } else {
      return "";
    }
  }
  
  /* GET heroes whose name contains search term */
  searchUsers(term: string): Observable<User[]> {
    if (!term.trim()) {
      // if not search term, return empty hero array.
      return of([]);
    }
    return this.http.get<User[]>(`${this.usersUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
         this.log(`found users matching "${term}"`) :
         this.log(`no users matching "${term}"`)),
      catchError(this.handleError<User[]>('searchUsers', []))
    );
  }



  getBasket(): Observable<Need[]> {
    const url = `${this.usersUrl}/${this.getCurrentUserName()}/basket`;
    const username = this.getCurrentUserName();
    return this.http.get<Need[]>(url, this.httpOptions).pipe(
        tap((needs : Need[]) => this.log( `fetched user basket ${this.currentUsername}`)),
        catchError(this.handleError<Need[]>('addtobasket'))
      );
  }
  addToBasket(need : Need): Observable<Need> {
    //8080/users/username/basketadd

    const url = `${this.usersUrl}/${this.getCurrentUserName()}/basketadd`;
    return this.http.post<Need>(url, need, this.httpOptions).pipe(
        tap((addedNeed : Need) => this.log( `added need w/ id=${addedNeed.id}`)),
        catchError(this.handleError<Need>('addtobasket'))
      );
      
  }

  addToHistory(need : Need[]): Observable<Need>{
    const url = `${this.usersUrl}/${this.getCurrentUserName()}/historyadd`;
    return this.http.post<Need>(url, need, this.httpOptions).pipe(
        tap((addedNeed : Need) => this.log( `added need w/ id=${addedNeed.id}`)),
        catchError(this.handleError<Need>('addtohistory'))
      );
  }

  removeFromBasket(id : number): Observable<Need[]> {
    //8080/users/username/basketremove
    const url = `${this.usersUrl}/${this.getCurrentUserName()}/basketremoveone/${id}`;
    return this.http.delete<Need[]>(url, this.httpOptions).pipe(
      
        tap((removedNeed: Need[]) => {
          this.log( `removed need w/ id=${id}`);
          
        }),
        catchError(this.handleError<Need[]>('removefrombasket'))
      );
  }
  checkout(): Observable<number> {
    const url = `${this.usersUrl}/${this.getCurrentUserName()}/checkout`;
    return this.http.delete<number>(url, this.httpOptions).pipe(
      tap(_ => this.log( `checked out ${this.getCurrentUserName}`))
    )
  }
  getTotal(): Observable<number>{
    const url = `${this.usersUrl}/${this.getCurrentUserName()}/total`;
    return this.http.get<number>(url, this.httpOptions).pipe(
      tap(_ => this.log(`total ${this.getCurrentUserName}`))
    )
  }

  getHistory(): Observable<Need[]>{
    const url = `${this.usersUrl}/${this.getCurrentUserName()}/history`;
    return this.http.get<Need[]>(url, this.httpOptions).pipe(
      tap(_ => this.log(`history ${this.getCurrentUserName}`))
    )
  }
  
  
  getTotalTwo(): number{
    return this.total;
  }

  //////// Save methods //////////

  /** POST: add a new hero to the server */
  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.usersUrl, user, this.httpOptions).pipe(
      tap((newUser: User) => this.log(`added user w/ id=${newUser.id}`)),
      catchError(this.handleError<User>('addUser'))
    );
  }

  /** DELETE: delete the hero from the server */
  deleteUser(username: string): Observable<User> {
    const url = `${this.usersUrl}/${username}`;

    return this.http.delete<User>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted user username=${username}`)),
      catchError(this.handleError<User>('deleteUser'))
    );
  }

  /** PUT: update the hero on the server */
  updateUser(user: User): Observable<any> {
    return this.http.put(this.usersUrl, user, this.httpOptions).pipe(
      tap(_ => this.log(`updated user id=${user.id}`)),
      catchError(this.handleError<any>('updateUser'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a HeroService message with the MessageService */
  private log(message: string) {
    this.messageService.add(`UserService: ${message}`);
  }
}