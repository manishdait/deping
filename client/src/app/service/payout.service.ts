import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { PayoutDto } from '../model/payout.type';

@Injectable({
  providedIn: 'root'
})
export class PayoutService {
  constructor(private client: HttpClient, private localstore: LocalStorageService) {}

  getPayouts(): Observable<PayoutDto> {
    return this.client.get<PayoutDto>(`http://localhost:8080/api/v1/payouts`, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }

  claimPayouts(): Observable<{[key: string]: number}> {
    return this.client.get<{[key: string]: number}>(`http://localhost:8080/api/v1/payouts/claim`, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }
}
