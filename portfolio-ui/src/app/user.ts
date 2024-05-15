import {Need} from './need';
export class User {
    public id: number = 0;
    public username: string = "";
    public password: string = "";
    public basket: Array<Need> = [];
    public history: Array<Need> = [];
    public total: number = 0;
    public constructor (id: number, username:string, password:string) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.basket = [];
        this.history = [];
    }
}
