import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { WebSiteDto, WebsiteRequest, WebsiteResponse } from '../model/website.type';
import { Observable } from 'rxjs';
import { LocalStorageService } from 'ngx-webstorage';
import { Page } from '../model/page.type';
import { TicksResponse } from '../model/ticks.type';

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

  fetchUserWebsite(id: number): Observable<WebSiteDto> {
    return this.client.get<WebSiteDto>(`http://localhost:8080/api/v1/websites/${id}`, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }

  fetchWebsitesTicks(id: number): Observable<TicksResponse[]> {
    return this.client.get<TicksResponse[]>(`http://localhost:8080/api/v1/websites/tick/${id}`, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }
}
