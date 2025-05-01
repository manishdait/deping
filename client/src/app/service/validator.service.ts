import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ValidatorDto } from '../model/validator.type';
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root'
})
export class ValidatorService {
  constructor(private client: HttpClient, private localstore: LocalStorageService) {}

  getValidator(): Observable<ValidatorDto> {
    return this.client.get<ValidatorDto>(`http://localhost:8080/api/v1/validators`, {headers: {
      'Authorization': 'Bearer ' + this.localstore.retrieve('accessToken')
    }});
  }
}
