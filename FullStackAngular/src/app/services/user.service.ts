import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = "http://localhost:8080/users";

  constructor(private httpClient: HttpClient) { }

  getUserList(): Observable<User[]>{
    return this.httpClient.get<User[]>(`${this.baseUrl}`);
  }

  getUserID (id: number): Observable<User> {
    return this.httpClient.get<User>(`${this.baseUrl}/${id}`);
  }

  public loginUserRemote(user: User): Observable<Object> {
    return this.httpClient.post(`${this.baseUrl}/login`, user)
  }

  addUser(user: User): Observable<Object>{
    return this.httpClient.post(`${this.baseUrl}`, user);
  }

  attUser(id: number, user: User): Observable<Object> {
    return this.httpClient.put(`${this.baseUrl}/${id}`, user);
  }

  deleteUser(id: number): Observable<Object>{
    return this.httpClient.delete(`${this.baseUrl}/${id}`);
  }



}
