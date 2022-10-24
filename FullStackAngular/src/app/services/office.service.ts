import { Office } from './../model/office';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class OfficeService {

  private baseUrl = "http://localhost:8080/office";

  constructor(private httpClient: HttpClient) { }

  getOfficeList(): Observable<Office[]>{
    return this.httpClient.get<Office[]>(`${this.baseUrl}`);
  }

  getOfficeID (id: number): Observable<Office> {
    return this.httpClient.get<Office>(`${this.baseUrl}/${id}`);
  }

  addOffice(office: Office): Observable<Object>{
    return this.httpClient.post(`${this.baseUrl}`, office);
  }

  attOffice(id: number, office: Office): Observable<Object> {
    return this.httpClient.put(`${this.baseUrl}/${id}`, office);
  }

  deleteOffice(id: number): Observable<Object>{
    return this.httpClient.delete(`${this.baseUrl}/${id}`);
  }
}
