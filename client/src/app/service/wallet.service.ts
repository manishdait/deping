import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { TransferHbarRequest, WalletDto } from '../model/wallet.type';

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  constructor(private client: HttpClient, private localstore: LocalStorageService) {}

  getWallet(): Observable<WalletDto> {
    return this.client.get<WalletDto>(`http://localhost:8080/api/v1/wallets`, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }

  transferHbar(req: TransferHbarRequest): Observable<WalletDto> {
    return this.client.post<WalletDto>(`http://localhost:8080/api/v1/wallets/transfer`, req, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }
}
