import { HttpBackend, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { LocalStorageService } from 'ngx-webstorage';
import { AuthRequest, AuthResponse, RegistrationRequest } from '../model/auth.type';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private client: HttpClient;

  constructor(private backend: HttpBackend, private localStore: LocalStorageService) {
    this.client = new HttpClient(backend);
  }

  registerUser(request: RegistrationRequest): Observable<AuthResponse> {
    return this.client.post<AuthResponse>(`http://localhost:8080/api/v1/auth/sign-up`, request).pipe(
      map(response => {
        this.storeCred(response);
        return response;
      })
    );
  }

  authenticateUser(request: AuthRequest): Observable<AuthResponse> {
    return this.client.post<AuthResponse>(`http://localhost:8080/api/v1/auth/login`, request).pipe(
      map(response => {
        this.storeCred(response);
        return response;
      })
    );
  }

  private storeCred(response: AuthResponse) {
    this.localStore.store('accessToken', response.accessToken);
    this.localStore.store('refreshToken', response.refreshToken);
    this.localStore.store('username', response.email);
    this.localStore.store('role', response.role);
  }
}
