import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { WebsiteRequest, WebsiteResponse } from '../model/website.type';
import { Observable } from 'rxjs';
import { LocalStorageService } from 'ngx-webstorage';
import { Page } from '../model/page.type';
import { TicksDto } from '../model/ticks.type';

@Injectable({
  providedIn: 'root'
})
export class WebsiteService {
  constructor(private client: HttpClient, private localstore: LocalStorageService) {}

  createWebsite(request: WebsiteRequest): Observable<WebsiteResponse> {
    return this.client.post<WebsiteResponse>(`http://localhost:8080/api/v1/websites`, request, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }

  fetchUserWebsites(): Observable<Page<WebsiteResponse>> {
    return this.client.get<Page<WebsiteResponse>>(`http://localhost:8080/api/v1/websites`, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }

  fetchWebsitesTicks(id: number): Observable<Page<TicksDto[]>> {
    return this.client.get<Page<TicksDto[]>>(`http://localhost:8080/api/v1/websites/tick/${id}`, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }
}
